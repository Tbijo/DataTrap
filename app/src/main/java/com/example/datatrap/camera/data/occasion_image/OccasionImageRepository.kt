package com.example.datatrap.camera.data.occasion_image

import java.io.File

class OccasionImageRepository(private val occasionImageDao: OccasionImageDao) {

    suspend fun insertImage(occasionImageEntity: OccasionImageEntity) {
        occasionImageDao.insertImage(occasionImageEntity)
    }

    suspend fun deleteImage(occasionImageEntity: OccasionImageEntity) {
        val myFile = File(occasionImageEntity.path)
        if (myFile.exists()) myFile.delete()
        occasionImageDao.deleteImage(occasionImageEntity)
    }

    suspend fun getImageForOccasion(occasionID: String): OccasionImageEntity? {
        return occasionImageDao.getImageForOccasion(occasionID)
    }
}