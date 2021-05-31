package me.rerere.polymartapp.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import me.rerere.polymartapp.const.PageLoadState
import me.rerere.polymartapp.model.resource.ResourceDetail
import me.rerere.polymartapp.model.user.UserManager
import me.rerere.polymartapp.repo.ResourceRepo
import javax.inject.Inject

@HiltViewModel
class ResourceViewModel @Inject constructor(
    private val userManager: UserManager,
    private val resourceRepo: ResourceRepo
) : ViewModel(){

    var resourceId = 0
    var loading by mutableStateOf(PageLoadState.IDLE)
    var detail by mutableStateOf(ResourceDetail.Loading)

    fun loadResource(resourceId: Int){
        this.resourceId =  0
    }

    fun load(){
        loading = PageLoadState.LOADING
        viewModelScope.launch {
            // TODO: LOAD RESOURCE DETAIL

            loading = PageLoadState.SUCCESS
        }
    }
}