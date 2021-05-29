package me.rerere.polymartapp.api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.rerere.polymartapp.api.base.ApiResult
import me.rerere.polymartapp.model.Cookie
import me.rerere.polymartapp.model.UserInfo
import me.rerere.polymartapp.model.server.Server
import me.rerere.polymartapp.model.server.ServerSort
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