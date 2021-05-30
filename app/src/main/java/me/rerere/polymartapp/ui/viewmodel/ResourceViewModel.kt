package me.rerere.polymartapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import me.rerere.polymartapp.model.user.UserManager
import me.rerere.polymartapp.repo.ResourceRepo
import javax.inject.Inject

@HiltViewModel
class ResourceViewModel @Inject constructor(
    private val userManager: UserManager,
    private val resourceRepo: ResourceRepo
) : ViewModel(){
    fun load(){

    }
}