package me.rerere.polymartapp.repo

import me.rerere.polymartapp.api.PolymartApi
import me.rerere.polymartapp.model.server.ServerSort
import javax.inject.Inject

class ServerRepo @Inject constructor(
    private val polymartApi: PolymartApi
){
    suspend fun getServerList(sort: ServerSort = ServerSort.BUMPED) = polymartApi.getServerList(sort)
}