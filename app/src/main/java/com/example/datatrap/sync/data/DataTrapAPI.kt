package com.example.datatrap.sync.data

import com.example.datatrap.models.sync.ImageSync
import com.example.datatrap.models.sync.SyncClass
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface DataTrapAPI {

    @GET("/api/data/{unixtime}")
    suspend fun downloadData(
        @Path("unixtime") unixTime: Long
    ): Response<List<SyncClass>>

    @POST("/api/data")
    suspend fun uploadData(
        @Body dataList: List<SyncClass>
    ): Response<Void>

    @POST("/api/images")
    suspend fun uploadImageInfo(
        @Body infoImages: ImageSync
    ): Response<Void>

    @Multipart
    @POST("/api/mFiles")
    suspend fun uploadMouseFiles(
        @Part files: List<MultipartBody.Part>
    ): Response<Void>

    @Multipart
    @POST("/api/oFiles")
    suspend fun uploadOccasionFiles(
        @Part files: List<MultipartBody.Part>
    ): Response<Void>

    @Multipart
    @POST("/api/sFiles")
    suspend fun uploadSpecieFiles(
        @Part files: List<MultipartBody.Part>
    ): Response<Void>

}