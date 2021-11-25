package com.example.datatrap.repositories

import androidx.lifecycle.LiveData
import com.example.datatrap.databaseio.dao.OccasionImageDao
import com.example.datatrap.models.OccasionImage

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
}