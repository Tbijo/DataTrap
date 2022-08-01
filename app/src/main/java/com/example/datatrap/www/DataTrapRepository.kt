package com.example.datatrap.www

import com.example.datatrap.models.sync.ImageSync
import com.example.datatrap.models.sync.SyncClass
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import java.io.File
import javax.inject.Inject

class DataTrapRepository @Inject constructor(
    private val api: DataTrapAPI
) {

    suspend fun getData(unixTime: Long): ResultWrapper<Response<List<SyncClass>>> {
        return safeApiCall {
            api.getData(unixTime)
        }
    }

    suspend fun insertData(dataList: List<SyncClass>): ResultWrapper<Response<Void>> {
        return safeApiCall {
            api.insertData(dataList)
        }
    }

    suspend fun insertImageInfo(infoImages: ImageSync): ResultWrapper<Response<Void>> {
        return safeApiCall {
            api.insertImageInfo(infoImages)
        }
    }

    suspend fun insertMouseFiles(files: List<File>): ResultWrapper<Response<Void>> {
        return safeApiCall {
            api.insertMouseFiles(
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

    suspend fun insertOccasionFiles(files: List<File>): ResultWrapper<Response<Void>> {
        return safeApiCall {
            api.insertOccasionFiles(
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

    suspend fun insertSpecieFiles(files: List<File>): ResultWrapper<Response<Void>> {
        return safeApiCall {
            api.insertSpecieFiles(
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