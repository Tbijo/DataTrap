package com.example.datatrap.www.utils

import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

sealed class ResultWrapper<out T> {
    data class MySuccess<out T>(val value: T) : ResultWrapper<T>()
    data class MyError(val code: Int? = null, val errorM: String) : ResultWrapper<Nothing>()
}

suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): ResultWrapper<Response<T>> =
    try {
        ResultWrapper.MySuccess(apiCall())
    } catch (throwable: Throwable) {
        when (throwable) {
            is IOException -> {
                ResultWrapper.MyError(errorM = "No internet connection or Stream closed or bad URL.")
            }
            is HttpException -> {
                val code = throwable.code()
                ResultWrapper.MyError(code, "Unexpected Response code not starting with 2.")
            }
            else -> {
                ResultWrapper.MyError(errorM = "Unsuspected Error.")
            }
        }
    }