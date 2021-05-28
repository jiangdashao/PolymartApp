package me.rerere.polymartapp.model.server

data class Server(
    val name: String,
    val ip: String,
    val overviewLink: String,
    val logo: String,
    val description: String,
    val online: Int,
    val maxOnline: Int
)
