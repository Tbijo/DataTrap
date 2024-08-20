package com.example.datatrap.sync.domain

import com.example.datatrap.sync.utils.NetworkError
import com.example.datatrap.sync.utils.Result
import com.example.datatrap.sync.utils.safeApiCall
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.Serializable

@Serializable
data class CensoredText(
    val result: String
)

class GetCoinsUseCase(
    private val httpClient: HttpClient,
) {
    operator fun invoke(): Flow<Result<CensoredText, NetworkError>> = flow {
        val result = safeApiCall<CensoredText> {
            httpClient.get(
                urlString = "https://www.purgomalum.com/service/json"
            ) {
                parameter("text", "uncensored text")
            }
        }

        emit(result)
    }
}