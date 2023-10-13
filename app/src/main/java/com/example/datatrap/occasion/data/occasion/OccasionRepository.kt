package com.example.datatrap.occasion.data.occasion

import kotlinx.coroutines.flow.Flow

class OccasionRepository(private val occasionDao: OccasionDao) {

    suspend fun insertOccasion(occasionEntity: OccasionEntity) {
        occasionDao.insertOccasion(occasionEntity)
    }

    suspend fun deleteOccasion(occasionEntity: OccasionEntity) {
        occasionDao.deleteOccasion(occasionEntity)
    }

    suspend fun getOccasion(occasionId: String): OccasionEntity {
        return occasionDao.getOccasion(occasionId)
    }

    fun getOccasionsForSession(idSession: String): Flow<List<OccasionEntity>> {
        return occasionDao.getOccasionsForSession(idSession)
    }

}