package me.rerere.polymartapp.api

import me.rerere.polymartapp.api.base.ApiResult
import me.rerere.polymartapp.model.Cookie
import me.rerere.polymartapp.model.server.Server
import me.rerere.polymartapp.model.UserInfo
import me.rerere.polymartapp.model.server.ServerSort

interface PolymartApi {
    /**
     * Login to polymart
     */
    suspend fun login(username: String, password: String): ApiResult<Cookie>

    /**
     * Get server list:
     * https://polymart.org/servers
     */
    suspend fun getServerList(sortBy: ServerSort = ServerSort.BUMPED): ApiResult<List<Server>>

    /**
     * Get user information
     */
    suspend fun getUseInfo(cookie: Cookie): ApiResult<UserInfo>
}