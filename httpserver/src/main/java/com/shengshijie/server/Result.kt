package com.shengshijie.server

sealed class Result<T> {

    data class Success<T>(val data: T) : Result<T>()

    data class Error<T>(val message: String) : Result<T>()

    companion object {

        fun <T> success(data: T) = Success(data)

        fun <T> error(message: String) = Error<T>(message)

    }

}