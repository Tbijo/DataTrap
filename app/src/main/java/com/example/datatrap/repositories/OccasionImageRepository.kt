package com.example.datatrap.repositories

import androidx.lifecycle.LiveData
import com.example.datatrap.databaseio.dao.OccasionImageDao
import com.example.datatrap.models.OccasionImage
import com.example.datatrap.models.sync.OccasionImageSync

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