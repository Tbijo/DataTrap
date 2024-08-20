package com.example.datatrap.sync.data.remote

import com.example.datatrap.core.util.Constants
import com.example.datatrap.sync.utils.NetworkError
import com.example.datatrap.sync.utils.Result
import com.example.datatrap.sync.utils.properText
import com.example.datatrap.sync.utils.safeApiCall
import io.ktor.client.HttpClient
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import java.io.File

class DataTrapRepository(
    private val httpClient: HttpClient,
) {
    suspend fun downloadData(unixTime: Long): Result<List<SyncClass>, NetworkError> {
        return safeApiCall<List<SyncClass>> {
            httpClient.get("${Constants.SERVER_URL}/api/data") {
                parameter("unixtime", unixTime)
            }
        }
    }

    suspend fun uploadData(dataList: List<SyncClass>): Result<Unit, NetworkError> {
        return safeApiCall<Unit> {
            httpClient.post("${Constants.SERVER_URL}/api/data") {
                contentType(ContentType.Application.Json)
                setBody(dataList)
            }
        }
    }

    suspend fun uploadImageInfo(infoImages: ImageSync): Result<Unit, NetworkError> {
        return safeApiCall<Unit> {
            httpClient.post("${Constants.SERVER_URL}/api/images") {
                contentType(ContentType.Application.Json)
                setBody(infoImages)
            }
        }
    }

    suspend fun uploadMouseFiles(files: List<File>): Result<Unit, NetworkError> {
        return safeApiCall<Unit> {
            httpClient.submitFormWithBinaryData(
                url = "${Constants.SERVER_URL}/api/mFiles",
                formData = formData {
                    files.forEach {
                        append(
                            key = it.name,
                            value = it.readBytes(),
                            headers = Headers.build {
                                append(HttpHeaders.ContentType, ContentType.Image.PNG.properText())
                            },
                        )
                    }
                },
            )
        }
    }

    suspend fun uploadOccasionFiles(files: List<File>): Result<Unit, NetworkError> {
        return safeApiCall<Unit> {
            httpClient.submitFormWithBinaryData(
                url = "${Constants.SERVER_URL}/api/oFiles",
                formData = formData {
                    files.forEach {
                        append(
                            key = it.name,
                            value = it.readBytes(),
                            headers = Headers.build {
                                append(HttpHeaders.ContentType, ContentType.Image.PNG.properText())
                            },
                        )
                    }
                },
            )
        }
    }

    suspend fun uploadSpecieFiles(files: List<File>): Result<Unit, NetworkError> {
        return safeApiCall<Unit> {
            httpClient.submitFormWithBinaryData(
                url = "${Constants.SERVER_URL}/api/sFiles",
                formData = formData {
                    files.forEach {
                        append(
                            key = it.name,
                            value = it.readBytes(),
                            headers = Headers.build {
                                append(HttpHeaders.ContentType, ContentType.Image.PNG.properText())
                            },
                        )
                    }
                },
            )
        }
    }

}