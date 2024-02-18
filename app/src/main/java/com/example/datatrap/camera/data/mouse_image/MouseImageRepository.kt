package com.example.datatrap.camera.data.mouse_image

class MouseImageRepository(private val mouseImageDao: MouseImageDao) {

    suspend fun insertImage(mouseImageEntity: MouseImageEntity) {
        mouseImageDao.insertImage(mouseImageEntity)
    }

    suspend fun deleteImage(mouseImageEntity: MouseImageEntity) {
        mouseImageDao.deleteImage(mouseImageEntity)
    }

    suspend fun getImageForMouse(mouseID: String): MouseImageEntity? {
        return mouseImageDao.getImageForMouse(mouseID)
    }

}