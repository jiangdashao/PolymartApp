package me.rerere.polymartapp.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import me.rerere.polymartapp.api.ResourceListSource
import me.rerere.polymartapp.model.resource.ResourceSort
import me.rerere.polymartapp.model.resource.searchResourceParams
import me.rerere.polymartapp.model.server.Server
import me.rerere.polymartapp.model.server.ServerSort
import me.rerere.polymartapp.model.user.NOT_LOGIN
import me.rerere.polymartapp.model.user.UserManager
import me.rerere.polymartapp.repo.ResourceRepo
import me.rerere.polymartapp.repo.ServerRepo
import me.rerere.polymartapp.repo.UserRepo
import javax.inject.Inject

@HiltViewModel
class IndexViewModel @Inject constructor(
    private val userManager: UserManager,
    private val userRepo: UserRepo,
    private val serverRepo: ServerRepo,
    private val resourceRepo: ResourceRepo
) : ViewModel() {

    // User Info
    var userInfo by mutableStateOf(NOT_LOGIN)

    // Resource List
    var resourceSort = MutableLiveData(ResourceSort.UPDATED)
    private val resourceSearchParam = searchResourceParams("host" to "polymart.org", "sort" to resourceSort.value!!.value)

    var resourceListPager = Pager(
        PagingConfig(
            pageSize = 18,
            initialLoadSize = 18
        )
    ){
        ResourceListSource(
            userManager,
            resourceRepo,
            resourceSearchParam
        )
    }.flow

    fun setResourceSort(resourceSort: ResourceSort){
        this.resourceSort.value = resourceSort
        resourceSearchParam.edit("sort" to resourceSort.value)
    }


    // Server List
    var sortType by mutableStateOf(ServerSort.BUMPED)
    var serverListIsLoading by mutableStateOf(false)
    var serverListError by mutableStateOf(false)
    var serverList: List<Server> by mutableStateOf(emptyList())

    init {
        refreshUserInfo()
        refreshServerList()
    }

    fun refreshUserInfo() {
        if (userManager.cookie.notEmpty()) {
            viewModelScope.launch {
                val result = userRepo.getUserInfo(userManager.cookie)
                if (result.isSuccess()) {
                    userManager.userInfo = result.value!!
                    userInfo = result.value!!
                }
            }
        }
    }

    fun refreshServerList() {
        serverListIsLoading = true
        serverListError = false
        viewModelScope.launch {
            val result = serverRepo.getServerList(sortType)
            if (result.isSuccess()) {
                serverList = result.value!!
            } else {
                serverListError = true
            }
            serverListIsLoading = false
        }
    }

}