package com.example.datatrap.camera.data.mouse_image

import java.io.File

class MouseImageRepository(private val mouseImageDao: MouseImageDao) {

    suspend fun insertImage(mouseImageEntity: MouseImageEntity) {
        mouseImageDao.insertImage(mouseImageEntity)
    }

    suspend fun deleteImage(mouseImageEntity: MouseImageEntity) {
        val myFile = File(mouseImageEntity.path)
        if (myFile.exists()) myFile.delete()
        mouseImageDao.deleteImage(mouseImageEntity)
    }

    suspend fun getImageForMouse(mouseID: String): MouseImageEntity? {
        return mouseImageDao.getImageForMouse(mouseID)
    }

}