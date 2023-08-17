package com.example.datatrap.sync.data

import okhttp3.MultipartBody
import retrofit2.http.*

interface DataTrapAPI {

    @GET("/api/data/{unixtime}")
    suspend fun downloadData(
        @Path("unixtime") unixTime: Long
    ): List<SyncClass>

    @POST("/api/data")
    suspend fun uploadData(
        @Body dataList: List<SyncClass>
    ): Void

    @POST("/api/images")
    suspend fun uploadImageInfo(
        @Body infoImages: ImageSync
    ): Void

    @Multipart
    @POST("/api/mFiles")
    suspend fun uploadMouseFiles(
        @Part files: List<MultipartBody.Part>
    ): Void

    @Multipart
    @POST("/api/oFiles")
    suspend fun uploadOccasionFiles(
        @Part files: List<MultipartBody.Part>
    ): Void

    @Multipart
    @POST("/api/sFiles")
    suspend fun uploadSpecieFiles(
        @Part files: List<MultipartBody.Part>
    ): Void

}