package com.example.datatrap.sync.data

import com.example.datatrap.models.sync.ImageSync
import com.example.datatrap.models.sync.SyncClass
import com.example.datatrap.sync.utils.ResultWrapper
import com.example.datatrap.sync.utils.safeApiCall
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import java.io.File
import javax.inject.Inject

class DataTrapRepository @Inject constructor(
    private val api: DataTrapAPI
) {

    suspend fun downloadData(unixTime: Long): ResultWrapper<Response<List<SyncClass>>> {
        return safeApiCall {
            api.downloadData(unixTime)
        }
    }

    suspend fun uploadData(dataList: List<SyncClass>): ResultWrapper<Response<Void>> {
        return safeApiCall {
            api.uploadData(dataList)
        }
    }

    suspend fun uploadImageInfo(infoImages: ImageSync): ResultWrapper<Response<Void>> {
        return safeApiCall {
            api.uploadImageInfo(infoImages)
        }
    }

    suspend fun uploadMouseFiles(files: List<File>): ResultWrapper<Response<Void>> {
        return safeApiCall {
            api.uploadMouseFiles(
                files = files.map {
                    // createFormData berie bud kluc a hodnotu ALEBO kluc, nazov file a requestBody
                    MultipartBody.Part.createFormData(
                        "image", // identifikacia pre server
                            it.name,
                            RequestBody.create(MediaType.parse("image/*"), it)
                    )
                }
            )
        }
    }

    suspend fun uploadOccasionFiles(files: List<File>): ResultWrapper<Response<Void>> {
        return safeApiCall {
            api.uploadOccasionFiles(
                files = files.map {
                    // createFormData berie bud kluc a hodnotu ALEBO kluc, nazov file a requestBody
                    MultipartBody.Part.createFormData(
                        "image", // identifikacia pre server
                        it.name,
                        RequestBody.create(MediaType.parse("image/*"), it)
                    )
                }
            )
        }
    }

    suspend fun uploadSpecieFiles(files: List<File>): ResultWrapper<Response<Void>> {
        return safeApiCall {
            api.uploadSpecieFiles(
                files = files.map {
                    // createFormData berie bud kluc a hodnotu ALEBO kluc, nazov file a requestBody
                    MultipartBody.Part.createFormData(
                        "image", // identifikacia pre server
                        it.name,
                        RequestBody.create(MediaType.parse("image/*"), it)
                    )
                }
            )
        }
    }

}