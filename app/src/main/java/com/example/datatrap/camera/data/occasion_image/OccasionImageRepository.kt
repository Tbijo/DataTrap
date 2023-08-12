package com.example.datatrap.camera.data.occasion_image

import androidx.lifecycle.LiveData
import com.example.datatrap.sync.data.OccasionImageSync

class OccasionImageRepository(private val occasionImageDao: OccasionImageDao) {

    suspend fun insertImage(occasionImageEntity: OccasionImageEntity) {
        occasionImageDao.insertImage(occasionImageEntity)
    }

    suspend fun deleteImage(occasionImgId: Long) {
        occasionImageDao.deleteImage(occasionImgId)
    }

    fun getImageForOccasion(occasionId: Long): LiveData<OccasionImageEntity> {
        return occasionImageDao.getImageForOccasion(occasionId)
    }

    suspend fun getOccasionImages(unixTime: Long): List<OccasionImageSync> {
        return occasionImageDao.getOccasionImages(unixTime)
    }
}