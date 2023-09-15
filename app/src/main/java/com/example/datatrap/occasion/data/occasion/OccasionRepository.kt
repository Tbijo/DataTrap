package com.example.datatrap.occasion.data.occasion

import kotlinx.coroutines.flow.Flow

class OccasionRepository(private val occasionDao: OccasionDao) {

    suspend fun insertOccasion(occasionEntity: OccasionEntity): String {
        return occasionDao.insertOccasion(occasionEntity)
    }

    suspend fun deleteOccasion(occasionEntity: OccasionEntity) {
        occasionDao.deleteOccasion(occasionEntity)
    }

    fun getOccasion(occasionId: String): Flow<OccasionEntity> {
        return occasionDao.getOccasion(occasionId)
    }

    fun getOccasionsForSession(idSession: String): Flow<List<OccasionEntity>> {
        return occasionDao.getOccasionsForSession(idSession)
    }

    // SYNC
    suspend fun getOccasionForSync(occasionId: List<String>): List<OccasionEntity> {
        return occasionDao.getOccasionForSync(occasionId)
    }

    suspend fun getSyncOccasion(sessionID: String, occasionStart: Long): OccasionEntity? {
        return occasionDao.getSyncOccasion(sessionID, occasionStart)
    }

}