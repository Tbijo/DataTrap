package com.example.datatrap.camera.data.mouse_image

import com.example.datatrap.sync.data.MouseImageSync
import kotlinx.coroutines.flow.Flow

class MouseImageRepository(private val mouseImageDao: MouseImageDao) {

    suspend fun insertImage(mouseImageEntity: MouseImageEntity) {
        mouseImageDao.insertImage(mouseImageEntity)
    }

    suspend fun deleteImage(mouseImageId: Long) {
        mouseImageDao.deleteImage(mouseImageId)
    }

    fun getImageForMouse(mouseIid: Long, deviceID: String): Flow<MouseImageEntity> {
        return mouseImageDao.getImageForMouse(mouseIid, deviceID)
    }

    suspend fun getMouseImages(unixTime: Long): List<MouseImageSync> {
        return mouseImageDao.getMouseImages(unixTime)
    }

}