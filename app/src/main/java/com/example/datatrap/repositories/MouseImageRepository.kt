package com.example.datatrap.repositories

import androidx.lifecycle.LiveData
import com.example.datatrap.databaseio.dao.MouseImageDao
import com.example.datatrap.models.MouseImage

class MouseImageRepository(private val mouseImageDao: MouseImageDao) {

    suspend fun insertImage(mouseImage: MouseImage) {
        mouseImageDao.insertImage(mouseImage)
    }

    suspend fun deleteImage(mouseImageId: Long) {
        mouseImageDao.deleteImage(mouseImageId)
    }

    fun getImageForMouse(mouseId: Long): LiveData<MouseImage> {
        return mouseImageDao.getImageForMouse(mouseId)
    }

}