package com.example.datatrap.sync.data.remote

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class DataTrapRepository(
    private val api: DataTrapAPI
) {

    suspend fun downloadData(unixTime: Long): List<SyncClass> {
        return api.downloadData(unixTime)
    }

    suspend fun uploadData(dataList: List<SyncClass>): Void {
        return api.uploadData(dataList)
    }

    suspend fun uploadImageInfo(infoImages: ImageSync): Void {
        return api.uploadImageInfo(infoImages)
    }

    suspend fun uploadMouseFiles(files: List<File>): Void {
        return api.uploadMouseFiles(
            files = files.map {
                // createFormData berie bud kluc a hodnotu ALEBO kluc, nazov file a requestBody
                MultipartBody.Part.createFormData(
                    "image", // identifikacia pre server
                    it.name,
                    RequestBody.create("image/*".toMediaTypeOrNull(), it)
                )
            }
        )
    }

    suspend fun uploadOccasionFiles(files: List<File>): Void {
        return api.uploadOccasionFiles(
            files = files.map {
                // createFormData berie bud kluc a hodnotu ALEBO kluc, nazov file a requestBody
                MultipartBody.Part.createFormData(
                    "image", // identifikacia pre server
                    it.name,
                    RequestBody.create("image/*".toMediaTypeOrNull(), it)
                )
            }
        )
    }

    suspend fun uploadSpecieFiles(files: List<File>): Void {
        return api.uploadSpecieFiles(
            files = files.map {
                // createFormData berie bud kluc a hodnotu ALEBO kluc, nazov file a requestBody
                MultipartBody.Part.createFormData(
                    "image", // identifikacia pre server
                    it.name,
                    RequestBody.create("image/*".toMediaTypeOrNull(), it)
                )
            }
        )
    }

}