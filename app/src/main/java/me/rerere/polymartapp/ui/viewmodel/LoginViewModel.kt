package me.rerere.polymartapp.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import me.rerere.polymartapp.repo.UserRepo
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepo: UserRepo
): ViewModel() {
    var username by mutableStateOf("")
    var password by mutableStateOf("")

    fun login(callback: (success: Boolean) -> Unit){
        viewModelScope.launch {
            val result = userRepo.login(username, password)
            callback(result.isSuccess())
        }
    }
}