package com.example.datatrap.occasion.data

import androidx.lifecycle.LiveData
import com.example.datatrap.occasion.domain.model.OccList
import com.example.datatrap.occasion.domain.model.OccasionView

class OccasionRepository(private val occasionDao: OccasionDao) {

    suspend fun insertOccasion(occasionEntity: OccasionEntity): Long {
        return occasionDao.insertOccasion(occasionEntity)
    }

    suspend fun updateOccasion(occasionEntity: OccasionEntity) {
        occasionDao.updateOccasion(occasionEntity)
    }

    suspend fun deleteOccasion(occasionId: Long) {
        occasionDao.deleteOccasion(occasionId)
    }

    fun getOccasion(occasionId: Long): LiveData<OccasionEntity> {
        return occasionDao.getOccasion(occasionId)
    }

    fun getOccasionView(occasionId: Long): LiveData<OccasionView> {
        return occasionDao.getOccasionView(occasionId)
    }

    fun getOccasionsForSession(idSession: Long): LiveData<List<OccList>> {
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