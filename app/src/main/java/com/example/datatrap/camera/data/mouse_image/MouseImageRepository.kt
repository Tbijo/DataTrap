package com.example.datatrap.camera.data.mouse_image

import androidx.lifecycle.LiveData
import com.example.datatrap.sync.data.sync.MouseImageSync

class MouseImageRepository(private val mouseImageDao: MouseImageDao) {

    suspend fun insertImage(mouseImage: MouseImage) {
        mouseImageDao.insertImage(mouseImage)
    }

    suspend fun deleteImage(mouseImageId: Long) {
        mouseImageDao.deleteImage(mouseImageId)
    }

    fun getImageForMouse(mouseIid: Long, deviceID: String): LiveData<MouseImage> {
        return mouseImageDao.getImageForMouse(mouseIid, deviceID)
    }

    suspend fun getMouseImages(unixTime: Long): List<MouseImageSync> {
        return mouseImageDao.getMouseImages(unixTime)
    }

}