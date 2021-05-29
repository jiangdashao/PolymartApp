package me.rerere.polymartapp.model

val NOT_LOGIN = UserInfo(-1, "Visitor", "", "Please login your account")

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
