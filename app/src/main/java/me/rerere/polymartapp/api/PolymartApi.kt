package me.rerere.polymartapp.api

import me.rerere.polymartapp.api.base.ApiResult
import me.rerere.polymartapp.model.resource.Resource
import me.rerere.polymartapp.model.resource.ResourceSearchParam
import me.rerere.polymartapp.model.server.Server
import me.rerere.polymartapp.model.server.ServerSort
import me.rerere.polymartapp.model.user.Cookie
import me.rerere.polymartapp.model.user.UserInfo

interface PolymartApi {
    /**
     * Login to polymart
     */
    suspend fun login(username: String, password: String): ApiResult<Cookie>

    /**
     * Check if it's a valid cookie
     */

    suspend fun isValidCookie(cookie: Cookie): Boolean

    /**
     * Get user information
     */
    suspend fun getUserInfo(cookie: Cookie): ApiResult<UserInfo>


    /**
     * Get resource list
     */
    suspend fun getResourceList(cookie: Cookie, resourceSearchParam: ResourceSearchParam): ApiResult<List<Resource>>

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