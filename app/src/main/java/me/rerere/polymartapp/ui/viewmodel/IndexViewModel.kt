package me.rerere.polymartapp.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import me.rerere.polymartapp.model.resource.Resource
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
    var resourceList by mutableStateOf(emptyList<Resource>())

    // Server List
    var sortType by mutableStateOf(ServerSort.BUMPED)
    var serverListIsLoading by mutableStateOf(false)
    var serverListError by mutableStateOf(false)
    var serverList: List<Server> by mutableStateOf(emptyList())

    init {
        refreshUserInfo()
        loadResourceList()
        refreshServerList()
    }

    fun loadResourceList() {
        viewModelScope.launch {
            val result = resourceRepo.getResources(
                userManager.cookie, searchResourceParams(

                )
            )
            if(result.isSuccess()){
                resourceList = result.value!!
            }
        }
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