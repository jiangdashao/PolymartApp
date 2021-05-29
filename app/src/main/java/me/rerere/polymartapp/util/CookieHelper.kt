package me.rerere.polymartapp.util

import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.OkHttpClient

class CookieJarHelper : CookieJar {
    var cookies: List<Cookie> = emptyList()

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        return if(url.isHttps && url.host == "polymart.org") {
            cookies
        } else emptyList()
    }

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        this.cookies = cookies
    }

    fun setCookie(cookie: me.rerere.polymartapp.model.Cookie) {
        cookies = listOf(
            Cookie.Builder()
                .name("session_token_id")
                .value(cookie.token_id ?: "")
                .domain("polymart.org")
                .build(),
            Cookie.Builder()
                .name("session_token_value")
                .value(cookie.token_value ?: "")
                .domain("polymart.org")
                .build()
        )
    }

    fun containsSessionCookie(): Boolean {
        val nameSet = cookies.map { it.name }
        return nameSet.contains("session_token_id") && nameSet.contains("session_token_value")
    }

    fun toCookie(): me.rerere.polymartapp.model.Cookie{
        require(containsSessionCookie())
        val map = HashMap<String, String>()
        cookies.forEach {
            map[it.name] = it.value
        }
        return me.rerere.polymartapp.model.Cookie(map["session_token_id"], map["session_token_value"])
    }
}