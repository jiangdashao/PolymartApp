package me.rerere.polymartapp.api

import me.rerere.polymartapp.model.Cookie
import me.rerere.polymartapp.model.UserInfo

interface PolymartApi {
    /**
     * Login to polymart
     */
    suspend fun login(username: String, password: String): ApiResult<Cookie>

    /**
     * Get user information
     */
    suspend fun getUseInfo(cookie: Cookie): ApiResult<UserInfo>
}