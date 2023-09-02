package com.example.datatrap.sync.utils

import java.io.IOException

suspend inline fun <reified T> safeApiCall(apiCall: () -> T): Resource<T> {

    val response: T

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
        val result = response

        result?.let {
            Resource.Success(it)
        } ?:
        Resource.Error(
            HttpException(
                HttpError.getError(response),
                "Null Error"
            )
        )

    }
    catch (e: Exception) {
        Resource.Error(
            HttpException(
                HttpError.getError(response),
                e.message ?: "Unknown"
            )
        )
    }
}