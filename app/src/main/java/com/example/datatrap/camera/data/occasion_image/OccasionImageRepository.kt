package com.example.datatrap.camera.data.occasion_image

import androidx.lifecycle.LiveData
import com.example.datatrap.sync.data.sync.OccasionImageSync

class OccasionImageRepository(private val occasionImageDao: OccasionImageDao) {

    suspend fun insertImage(occasionImage: OccasionImage) {
        occasionImageDao.insertImage(occasionImage)
    }

    suspend fun deleteImage(occasionImgId: Long) {
        occasionImageDao.deleteImage(occasionImgId)
    }

    fun getImageForOccasion(occasionId: Long): LiveData<OccasionImage> {
        return occasionImageDao.getImageForOccasion(occasionId)
    }

    suspend fun getOccasionImages(unixTime: Long): List<OccasionImageSync> {
        return occasionImageDao.getOccasionImages(unixTime)
    }
}