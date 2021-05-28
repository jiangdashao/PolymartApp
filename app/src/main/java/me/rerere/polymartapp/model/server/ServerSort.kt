package me.rerere.polymartapp.model.server

enum class ServerSort(
    val value: String
) {
    BUMPED("bumped"),
    RELEVANT("relevant"),
    CREATED("created"),
    VOTES("votes"),
    RANDOM("random")
}