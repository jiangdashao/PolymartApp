package me.rerere.polymartapp.repo

import me.rerere.polymartapp.api.PolymartApi
import me.rerere.polymartapp.api.base.ApiResult
import me.rerere.polymartapp.model.user.Cookie
import javax.inject.Inject

class UserRepo @Inject constructor(
    private val polymartApi: PolymartApi
) {
    suspend fun login(username: String, password: String): ApiResult<Cookie> = polymartApi.login(username, password)

    suspend fun isValid(cookie: Cookie) = polymartApi.isValidCookie(cookie)

    suspend fun getUserInfo(cookie: Cookie) = polymartApi.getUserInfo(cookie)
}