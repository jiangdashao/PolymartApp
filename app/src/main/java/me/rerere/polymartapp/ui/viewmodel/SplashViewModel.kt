package me.rerere.polymartapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import me.rerere.polymartapp.model.user.Cookie
import me.rerere.polymartapp.model.user.UserManager
import me.rerere.polymartapp.repo.UserRepo
import javax.inject.Inject

private const val TAG = "SplashViewModel"

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val userManager: UserManager,
    private val userRepo: UserRepo,
) : ViewModel(){
    fun checkCookie() = viewModelScope.launch {
        if(userManager.cookie.notEmpty()){
            val isValid = userRepo.isValid(userManager.cookie)
            if(!isValid){
                userManager.cookie = Cookie("", "")
                userManager.saveCookie()
                Log.i(TAG, "checkCookie: Your cookie expired!")
            }
        }
    }
}