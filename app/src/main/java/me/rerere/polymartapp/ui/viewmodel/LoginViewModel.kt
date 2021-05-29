package me.rerere.polymartapp.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import me.rerere.polymartapp.model.UserManager
import me.rerere.polymartapp.repo.UserRepo
import javax.inject.Inject

private const val TAG = "LoginViewModel"

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepo: UserRepo,
    private val userManager: UserManager
) : ViewModel() {
    var username by mutableStateOf("")
    var password by mutableStateOf("")

    fun login(callback: (success: Boolean) -> Unit) {
        viewModelScope.launch {
            val result = userRepo.login(username, password)
            if (result.isSuccess()) {
                val userInfo = userRepo.getUserInfo(result.value!!)
                if (userInfo.isSuccess()) {
                    callback(true)

                    userManager.cookie = result.value!!
                    userManager.saveCookie()
                    userManager.userInfo = userInfo.value!!

                    Log.i(TAG, "login: Login Successful")
                } else {
                    Log.i(TAG, "login: Failed Login(Failed to get user info)")
                    callback(false)
                }
            } else {
                Log.i(TAG, "login: Failed Login(Failed to get cookie)")
                callback(false)
            }
        }
    }
}