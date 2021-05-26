package me.rerere.polymartapp.model

data class UserInfo(
    // User Id
    val userId: Int,

    // Nickname
    var nickname: String,

    // Profile Pic link
    var profilePic: String,

    // Biography
    var biography: String
)
