package me.rerere.polymartapp.repo

import me.rerere.polymartapp.api.PolymartApi
import me.rerere.polymartapp.model.resource.ResourceSearchParam
import me.rerere.polymartapp.model.user.Cookie
import javax.inject.Inject

class ResourceRepo @Inject constructor(
    private val polymartApi: PolymartApi
) {
    suspend fun getResources(cookie: Cookie, resourceSearchParam: ResourceSearchParam) = polymartApi.getResourceList(cookie, resourceSearchParam)
}