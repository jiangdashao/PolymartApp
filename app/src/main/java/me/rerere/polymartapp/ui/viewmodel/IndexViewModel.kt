package me.rerere.polymartapp.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import me.rerere.polymartapp.model.server.Server
import me.rerere.polymartapp.model.server.ServerSort
import me.rerere.polymartapp.repo.ServerRepo
import javax.inject.Inject

@HiltViewModel
class IndexViewModel @Inject constructor(
    private val serverRepo: ServerRepo
): ViewModel(){
    // Server List
    var sortType by mutableStateOf(ServerSort.BUMPED)
    var serverListIsLoading by mutableStateOf(false)
    var serverListError by mutableStateOf(false)
    var serverList: List<Server> by mutableStateOf(emptyList())

    init {
        refreshServerList()
    }

    fun refreshServerList(){
        serverListIsLoading = true
        serverListError = false
        viewModelScope.launch {
            val result = serverRepo.getServerList(sortType)
            if(result.isSuccess()){
                serverList = emptyList()
                serverList = result.value!!
            }else {
                serverListError = true
            }
            serverListIsLoading = false
        }
    }

}