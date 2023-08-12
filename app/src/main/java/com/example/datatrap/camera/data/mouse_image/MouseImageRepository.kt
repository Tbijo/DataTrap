package com.example.datatrap.camera.data.mouse_image

import androidx.lifecycle.LiveData
import com.example.datatrap.sync.data.MouseImageSync

class MouseImageRepository(private val mouseImageDao: MouseImageDao) {

    suspend fun insertImage(mouseImageEntity: MouseImageEntity) {
        mouseImageDao.insertImage(mouseImageEntity)
    }

    suspend fun deleteImage(mouseImageId: Long) {
        mouseImageDao.deleteImage(mouseImageId)
    }

    fun getImageForMouse(mouseIid: Long, deviceID: String): LiveData<MouseImageEntity> {
        return mouseImageDao.getImageForMouse(mouseIid, deviceID)
    }

    suspend fun getMouseImages(unixTime: Long): List<MouseImageSync> {
        return mouseImageDao.getMouseImages(unixTime)
    }

}