package me.rerere.polymartapp.model.user

import android.content.Context
import androidx.core.content.edit
import me.rerere.polymartapp.PolymartApp

object UserManager {
    var cookie: Cookie
    var userInfo: UserInfo = NOT_LOGIN
        set(value) {
            field = value
            requireRefresh = true
        }
    var requireRefresh = false

    init {
        cookie = loadCookieFromLocal()
    }

    fun saveCookie(){
        val sharedPreferences = PolymartApp.instance.getSharedPreferences("cookie", Context.MODE_PRIVATE)
        sharedPreferences.edit {
            putString("id", cookie.token_id)
            putString("value", cookie.token_value)
        }
    }

    private fun loadCookieFromLocal(): Cookie {
        val sharedPreferences = PolymartApp.instance.getSharedPreferences("cookie", Context.MODE_PRIVATE)
        val id = sharedPreferences.getString("id", "")
        val value = sharedPreferences.getString("value", "")
        return Cookie(id, value)
    }
}