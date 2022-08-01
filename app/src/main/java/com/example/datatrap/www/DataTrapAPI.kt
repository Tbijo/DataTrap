package com.example.datatrap.www

import com.example.datatrap.models.sync.ImageSync
import com.example.datatrap.models.sync.SyncClass
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface DataTrapAPI {

    @GET("/api/data/{unixtime}")
    suspend fun getData(
        @Path("unixtime") unixTime: Long
    ): Response<List<SyncClass>>

    @POST("/api/data")
    suspend fun insertData(
        @Body dataList: List<SyncClass>
    ): Response<Void>

    @POST("/api/images")
    suspend fun insertImageInfo(
        @Body infoImages: ImageSync
    ): Response<Void>

    @Multipart
    @POST("/api/mFiles")
    suspend fun insertMouseFiles(
        @Part files: List<MultipartBody.Part>
    ): Response<Void>

    @Multipart
    @POST("/api/oFiles")
    suspend fun insertOccasionFiles(
        @Part files: List<MultipartBody.Part>
    ): Response<Void>

    @Multipart
    @POST("/api/sFiles")
    suspend fun insertSpecieFiles(
        @Part files: List<MultipartBody.Part>
    ): Response<Void>

}