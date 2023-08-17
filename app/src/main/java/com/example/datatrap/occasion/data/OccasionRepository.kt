package com.example.datatrap.occasion.data

import com.example.datatrap.occasion.domain.model.OccList
import com.example.datatrap.occasion.domain.model.OccasionView
import kotlinx.coroutines.flow.Flow

class OccasionRepository(private val occasionDao: OccasionDao) {

    suspend fun insertOccasion(occasionEntity: OccasionEntity): Long {
        return occasionDao.insertOccasion(occasionEntity)
    }

    suspend fun deleteOccasion(occasionId: Long) {
        occasionDao.deleteOccasion(occasionId)
    }

    fun getOccasion(occasionId: Long): Flow<OccasionEntity> {
        return occasionDao.getOccasion(occasionId)
    }

    fun getOccasionView(occasionId: Long): Flow<OccasionView> {
        return occasionDao.getOccasionView(occasionId)
    }

    fun getOccasionsForSession(idSession: Long): Flow<List<OccList>> {
        return occasionDao.getOccasionsForSession(idSession)
    }

    // SYNC
    suspend fun getOccasionForSync(occasionId: List<Long>): List<OccasionEntity> {
        return occasionDao.getOccasionForSync(occasionId)
    }

    suspend fun getSyncOccasion(sessionID: Long, occasionStart: Long): OccasionEntity? {
        return occasionDao.getSyncOccasion(sessionID, occasionStart)
    }

}