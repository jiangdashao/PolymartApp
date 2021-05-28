package me.rerere.polymartapp.api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.rerere.polymartapp.api.base.ApiResult
import me.rerere.polymartapp.model.Cookie
import me.rerere.polymartapp.model.server.Server
import me.rerere.polymartapp.model.UserInfo
import me.rerere.polymartapp.model.server.ServerSort
import me.rerere.polymartapp.util.CookieJarHelper
import okhttp3.*
import org.jsoup.Jsoup

class PolymartApiImpl(
    private val httpClient: OkHttpClient
) : PolymartApi {
    override suspend fun login(username: String, password: String): ApiResult<Cookie> = withContext(Dispatchers.IO) {
        try {
            val cookie = httpClient.cookieJar as CookieJarHelper
            cookie.cookies = emptyList()

            val request = Request.Builder()
                .url(POLYMART_BASEURL, "login")
                .post(FormBody.Builder()
                    .add("loginCredential", username)
                    .add("password", password)
                    .add("remember_me", "on")
                    .add("submit","Login")
                    .build())
                .build()

            val response = httpClient.newCall(request).execute()

            // return
            if(response.code == 200 && cookie.containsSessionCookie()) ApiResult.success(cookie.toCookie()) else ApiResult.failed()
        } catch (exception: Exception) {
            ApiResult.failed()
        }
    }

    override suspend fun getServerList(sortBy: ServerSort): ApiResult<List<Server>> = withContext(Dispatchers.IO){
        try {
            val request = Request.Builder()
                .url(POLYMART_BASEURL, "servers?sort=${sortBy.value}&")
                .get()
                .build()

            val response = httpClient.newCall(request).execute()
            val content = response.body?.string() ?: ""

            if(content.isEmpty()){
                ApiResult.failed()
            }else{
                val document = Jsoup.parse(content)
                val body = document.body()
                val grid = body.getElementById("server-grid")
                val list: List<Server> = grid.children().map {
                    val descElement = it.child(0).child(1)
                    val name = descElement.child(0).html().trim()
                    val description = descElement.child(2).html().trim()

                    val onlineData = descElement.child(1).text().trim().split(" / ")
                    val online = try {onlineData[0].toInt()} catch (e: java.lang.Exception){-1}
                    val maxOnline = try {onlineData[1].toInt()} catch (e: java.lang.Exception){-1}

                    val ip = it.child(0).child(2).child(0).child(0).child(0).child(0).text().trim()
                    val overviewLink = it.attr("href")
                    val logo = it.child(0).child(0).child(0).attr("data")

                    Server(name, ip, overviewLink, logo, description, online, maxOnline)
                }
                ApiResult.success(list)
            }
        } catch (e: java.lang.Exception){
            ApiResult.failed()
        }
    }

    override suspend fun getUseInfo(cookie: Cookie): ApiResult<UserInfo> = withContext(Dispatchers.IO){
        ApiResult.failed()
    }
}