package com.example.datatrap.sync.utils

import retrofit2.Response
import java.io.IOException

sealed class Resource<T>(val data: T?, val throwable: Throwable? = null) {

    class Success<T>(data: T): Resource<T>(data)

    class Error<T>(throwable: Throwable): Resource<T>(null, throwable)

    class Loading<T>(message: String? = null, data: T? = null): Resource<T>(data = data)
}

suspend inline fun <reified T> safeApiCall(apiCall: () -> Response<T>): Resource<T> {

    val response: Response<T>

    try {
        response = apiCall()
    }
    catch (e: IOException) {
        return Resource.Error(
            HttpException(
                HttpError.SERVICE_UNAVAILABLE,
                e.message ?: "Unknown"
            )
        )
    }

    return try {
        val result = response.body()

        result?.let {
            Resource.Success(it)
        } ?:
        Resource.Error(
            HttpException(
                HttpError.getError(response.code()),
                "Null Error"
            )
        )

    }
    catch (e: Exception) {
        Resource.Error(
            HttpException(
                HttpError.getError(response.code()),
                e.message ?: "Unknown"
            )
        )
    }
}