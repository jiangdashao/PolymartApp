package me.rerere.polymartapp.model.user

data class Cookie(
    var token_id: String?,
    var token_value: String?
){
    fun notEmpty() = !token_id.isNullOrEmpty() && !token_value.isNullOrEmpty()
}