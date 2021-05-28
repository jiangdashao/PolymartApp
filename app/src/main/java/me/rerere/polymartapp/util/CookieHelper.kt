package me.rerere.polymartapp.util

import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.OkHttpClient

class CookieJarHelper : CookieJar {
    var cookies: List<Cookie> = emptyList()

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        return emptyList()
    }

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        this.cookies = cookies
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