package me.rerere.polymartapp.api.base

sealed class ApiResult<T>(val value: T?){
    class Success<T>(value: T): ApiResult<T>(value)

    class Failed<T>(): ApiResult<T>(null)

    fun isSuccess() = this is Success

    companion object {
        fun <T> success(value: T): Success<T> = Success(value)

        fun <T> failed(): Failed<T> = Failed()
    }
}