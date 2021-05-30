package me.rerere.polymartapp.api

import androidx.paging.PagingSource
import androidx.paging.PagingState
import me.rerere.polymartapp.model.resource.Resource
import me.rerere.polymartapp.model.resource.ResourceSearchParam
import me.rerere.polymartapp.model.resource.searchResourceParams
import me.rerere.polymartapp.model.user.UserManager
import me.rerere.polymartapp.repo.ResourceRepo

class ResourceListSource constructor(
    private val userManager: UserManager,
    private val resourceRepo: ResourceRepo,
    private val listOption: ResourceSearchParam = searchResourceParams(
        "host" to "polymart.org"
    )
) : PagingSource<Int, Resource>() {
    override fun getRefreshKey(state: PagingState<Int, Resource>): Int? {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Resource> {
        val currentPage = params.key ?: 0
        val startIndex = currentPage * params.loadSize

        println("Load: $currentPage & ${params.loadSize}")

        val resourceList = resourceRepo.getResources(
            userManager.cookie, listOption.edit(
                "start" to startIndex.toString(),
                "limit" to params.loadSize.toString()
            )
        )

        return if (resourceList.isSuccess()) {
            LoadResult.Page(
                data = resourceList.value!!,
                prevKey = if(currentPage > 0) currentPage - 1 else null,
                nextKey = currentPage + 1
            )
        } else {
            LoadResult.Error(Exception("Failed to load resource list"))
        }
    }
}