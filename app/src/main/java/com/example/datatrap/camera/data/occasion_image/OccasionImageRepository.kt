package com.example.datatrap.camera.data.occasion_image

class OccasionImageRepository(private val occasionImageDao: OccasionImageDao) {

    suspend fun insertImage(occasionImageEntity: OccasionImageEntity) {
        occasionImageDao.insertImage(occasionImageEntity)
    }

    suspend fun deleteImage(occasionImageEntity: OccasionImageEntity) {
        occasionImageDao.deleteImage(occasionImageEntity)
    }

    suspend fun getImageForOccasion(occasionID: String): OccasionImageEntity? {
        return occasionImageDao.getImageForOccasion(occasionID)
    }
}