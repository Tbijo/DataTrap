package com.example.datatrap.camera.data.occasion_image

import com.example.datatrap.sync.data.OccasionImageSync
import kotlinx.coroutines.flow.Flow

class OccasionImageRepository(private val occasionImageDao: OccasionImageDao) {

    suspend fun insertImage(occasionImageEntity: OccasionImageEntity) {
        occasionImageDao.insertImage(occasionImageEntity)
    }

    suspend fun deleteImage(occasionImageEntity: OccasionImageEntity) {
        occasionImageDao.deleteImage(occasionImageEntity)
    }

    fun getImageForOccasion(occasionId: String): Flow<OccasionImageEntity?> {
        return occasionImageDao.getImageForOccasion(occasionId)
    }

    suspend fun getOccasionImages(unixTime: Long): List<OccasionImageSync> {
        return occasionImageDao.getOccasionImages(unixTime)
    }
}