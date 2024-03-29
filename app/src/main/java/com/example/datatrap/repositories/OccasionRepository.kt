package com.example.datatrap.repositories

import androidx.lifecycle.LiveData
import com.example.datatrap.databaseio.dao.OccasionDao
import com.example.datatrap.models.Occasion
import com.example.datatrap.models.tuples.OccList
import com.example.datatrap.models.tuples.OccasionView

class OccasionRepository(private val occasionDao: OccasionDao) {

    suspend fun insertOccasion(occasion: Occasion): Long {
        return occasionDao.insertOccasion(occasion)
    }

    suspend fun updateOccasion(occasion: Occasion) {
        occasionDao.updateOccasion(occasion)
    }

    suspend fun deleteOccasion(occasionId: Long) {
        occasionDao.deleteOccasion(occasionId)
    }

    fun getOccasion(occasionId: Long): LiveData<Occasion> {
        return occasionDao.getOccasion(occasionId)
    }

    fun getOccasionView(occasionId: Long): LiveData<OccasionView> {
        return occasionDao.getOccasionView(occasionId)
    }

    fun getOccasionsForSession(idSession: Long): LiveData<List<OccList>> {
        return occasionDao.getOccasionsForSession(idSession)
    }

    // SYNC
    suspend fun getOccasionForSync(occasionId: List<Long>): List<Occasion> {
        return occasionDao.getOccasionForSync(occasionId)
    }

    suspend fun getSyncOccasion(sessionID: Long, occasionStart: Long): Occasion? {
        return occasionDao.getSyncOccasion(sessionID, occasionStart)
    }

}