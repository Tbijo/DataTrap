package com.example.datatrap.core.util

sealed class Resource<T>(val data: T?, val throwable: Throwable? = null) {
    class Success<T>(data: T): Resource<T>(data)

    class Error<T>(throwable: Throwable): Resource<T>(null, throwable)

    //class Loading<T>(message: String? = null, data: T? = null): Resource<T>(data = data)
}