package com.example.datatrap.sync.utils

enum class HttpError {
    SERVICE_UNAVAILABLE,
    CLIENT_ERROR, // cant reach server - wrong request
    SERVER_ERROR, // crash on server
    UNKNOWN_ERROR;

    companion object {
        fun getError(status: Int): HttpError {
            return when(status) {
                in 500..599 -> SERVER_ERROR
                in 400..499 -> CLIENT_ERROR
                else -> UNKNOWN_ERROR
            }
        }
    }
}

class HttpException(
    private val sourceException: HttpError,
    val actualMessage: String
): Exception(
    "Source of exception: $sourceException"
)