package me.rerere.polymartapp.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.rerere.polymartapp.model.NOT_LOGIN
import me.rerere.polymartapp.model.UserManager
import me.rerere.polymartapp.model.server.Server
import me.rerere.polymartapp.model.server.ServerSort
import me.rerere.polymartapp.repo.ServerRepo
import me.rerere.polymartapp.repo.UserRepo
import javax.inject.Inject

@HiltViewModel
class IndexViewModel @Inject constructor(
    private val userManager: UserManager,
    private val userRepo: UserRepo,
    private val serverRepo: ServerRepo
) : ViewModel() {

    // User Info
    var userInfo by mutableStateOf(NOT_LOGIN)

    // Server List
    var sortType by mutableStateOf(ServerSort.BUMPED)
    var serverListIsLoading by mutableStateOf(false)
    var serverListError by mutableStateOf(false)
    var serverList: List<Server> by mutableStateOf(emptyList())

    init {
        refreshServerList()
        refreshUserInfo()

        viewModelScope.launch {
            while (true){
                if(userInfo != userManager.userInfo) {
                    userInfo = userManager.userInfo
                    delay(5000)
                }
                delay(1000)
            }
        }
    }

    fun refreshUserInfo() {
        if(userManager.cookie.notEmpty()){
            viewModelScope.launch {
                val result = userRepo.getUserInfo(userManager.cookie)
                if(result.isSuccess()){
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
                serverList = emptyList()
                serverList = result.value!!
            } else {
                serverListError = true
            }
            serverListIsLoading = false
        }
    }

}