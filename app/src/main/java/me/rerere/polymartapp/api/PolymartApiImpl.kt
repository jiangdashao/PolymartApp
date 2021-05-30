package me.rerere.polymartapp.api

import androidx.compose.ui.graphics.Color
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.rerere.polymartapp.api.base.ApiResult
import me.rerere.polymartapp.model.resource.Resource
import me.rerere.polymartapp.model.resource.ResourceSearchParam
import me.rerere.polymartapp.model.resource.SearchResponse
import me.rerere.polymartapp.model.server.Server
import me.rerere.polymartapp.model.server.ServerSort
import me.rerere.polymartapp.model.user.Cookie
import me.rerere.polymartapp.model.user.UserInfo
import me.rerere.polymartapp.util.CookieJarHelper
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

private const val TAG = "PolymartApiImpl"

class PolymartApiImpl(
    private val httpClient: OkHttpClient
) : PolymartApi {
    private val gson = Gson()

    override suspend fun login(username: String, password: String): ApiResult<Cookie> =
        withContext(Dispatchers.IO) {
            try {
                val cookie = httpClient.cookieJar as CookieJarHelper
                cookie.cookies = emptyList()

                val request = Request.Builder()
                    .url(POLYMART_BASEURL, "login")
                    .post(
                        FormBody.Builder()
                            .add("loginCredential", username)
                            .add("password", password)
                            .add("remember_me", "on")
                            .add("submit", "Login")
                            .build()
                    )
                    .build()

                val response = httpClient.newCall(request).execute()
                // return
                if (response.code == 200 && cookie.containsSessionCookie()) ApiResult.success(cookie.toCookie()) else ApiResult.failed()
            } catch (exception: Exception) {
                exception.printStackTrace()
                ApiResult.failed()
            }
        }

    override suspend fun isValidCookie(cookie: Cookie): Boolean = withContext(Dispatchers.IO) {
        try {
            (httpClient.cookieJar as CookieJarHelper).setCookie(cookie)
            val request = Request.Builder()
                .url(POLYMART_BASEURL, "account")
                .build()

            httpClient.newCall(request).execute()

            // If it's redirect to the login page, so the cookie expired or invalid
            !request.url.toString().contains("login")
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override suspend fun getUserInfo(cookie: Cookie): ApiResult<UserInfo> =
        withContext(Dispatchers.IO) {
            try {
                (httpClient.cookieJar as CookieJarHelper).setCookie(cookie)
                val accountRequest = Request.Builder()
                    .url(POLYMART_BASEURL, "account")
                    .get()
                    .build()
                val response = httpClient.newCall(accountRequest).execute()
                val document = Jsoup.parse(response.body?.string() ?: error("empty body: response"))
                val body: Element = document.body()
                val link = body.getElementsByAttributeValue("title", "You").attr("href")

                val infoRequest = Request.Builder()
                    .url("https://polymart.org$link")
                    .get()
                    .build()

                val infoResponse = httpClient.newCall(infoRequest).execute()
                val infoBody =
                    Jsoup.parse(infoResponse.body?.string() ?: error("empty body: infoResponse"))
                        .body()
                val mainContent = infoBody.getElementById("main-content")

                val userId = link.split("/").last().toInt()
                val nickName = mainContent.getElementsByTag("h1").text()
                val profilePic = mainContent.getElementById("thumbnail-image").attr("src")
                val biography = mainContent.getElementById("biography-contents")

                ApiResult.success(UserInfo(userId, nickName, profilePic, biography.ownText()))
            } catch (e: Exception) {
                e.printStackTrace()
                ApiResult.failed()
            }
        }

    override suspend fun getResourceList(
        cookie: Cookie,
        resourceSearchParam: ResourceSearchParam
    ): ApiResult<List<Resource>> = withContext(Dispatchers.IO) {
        try {
            (httpClient.cookieJar as CookieJarHelper).setCookie(cookie)

            // 首先访问 "https://polymart.org/resources", 解析出API地址
            val authRequest = Request.Builder()
                .url("https://polymart.org/resources")
                .get()
                .build()
            val authResponse = httpClient.newCall(authRequest).execute()
            val mainContent = Jsoup.parse(authResponse.body?.string() ?: error("empty body"))
                .getElementById("main-content")
            val jsPart = mainContent.child(0).child(0).html()
            val startIndex = jsPart.indexOf("https://api.polymart.org")
            val endIndex = jsPart.indexOf("`", startIndex)
            val requestLink = jsPart.substring(startIndex until endIndex)
                .replace("send_html=1&", "") // Return json data, not html

            // 基于该API地址获取资源列表
            val request = Request.Builder()
                .url(requestLink)
                .post(resourceSearchParam.toFormBody())
                .build()
            val response = httpClient.newCall(request).execute()
            val responseObj =
                gson.fromJson(response.body?.string() ?: "", SearchResponse::class.java)

            // 将响应结果转换为列表
            if (responseObj.response.success) {
                return@withContext ApiResult.success(
                    responseObj.response.result.map {
                        Resource(
                            it.id.toInt(),
                            it.title,
                            it.subtitle,
                            it.owner.id.toInt(),
                            it.owner.name,
                            it.thumbnailURL,
                            Color(("ff" + it.themeColorLight).toLong(16)),
                            Color(("ff" + it.themeColorDark).toLong(16)),
                            it.price,
                            it.currency,
                            it.version,
                            it.totalDownloads.toInt()
                        )
                    }
                )
            }

            ApiResult.failed()
        } catch (exception: Exception) {
            exception.printStackTrace()
            ApiResult.failed()
        }
    }

    override suspend fun getServerList(sortBy: ServerSort): ApiResult<List<Server>> =
        withContext(Dispatchers.IO) {
            try {
                val request = Request.Builder()
                    .url(POLYMART_BASEURL, "servers?sort=${sortBy.value}&")
                    .get()
                    .build()

                val response = httpClient.newCall(request).execute()
                val content = response.body?.string() ?: ""

                if (content.isEmpty()) {
                    ApiResult.failed()
                } else {
                    val document = Jsoup.parse(content)
                    val body = document.body()
                    val grid = body.getElementById("server-grid")
                    val list: List<Server> = grid.children().map {
                        val descElement = it.child(0).child(1)
                        val name = descElement.child(0).html().trim()
                        val description = descElement.child(2).html().trim()

                        val onlineData = descElement.child(1).text().trim().split(" / ")
                        val online = try {
                            onlineData[0].toInt()
                        } catch (e: java.lang.Exception) {
                            -1
                        }
                        val maxOnline = try {
                            onlineData[1].toInt()
                        } catch (e: java.lang.Exception) {
                            -1
                        }

                        val ip =
                            it.child(0).child(2).child(0).child(0).child(0).child(0).text().trim()
                        val overviewLink = it.attr("href")
                        val logo = it.child(0).child(0).child(0).attr("data")

                        Server(name, ip, overviewLink, logo, description, online, maxOnline)
                    }
                    ApiResult.success(list)
                }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
                ApiResult.failed()
            }
        }

    override suspend fun getUseInfo(cookie: Cookie): ApiResult<UserInfo> =
        withContext(Dispatchers.IO) {
            ApiResult.failed()
        }
}