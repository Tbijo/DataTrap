package com.example.datatrap.repositories

import com.example.datatrap.databaseio.dao.OccasionDao
import com.example.datatrap.models.Occasion
import kotlinx.coroutines.flow.Flow

class OccasionRepository(private val occasionDao: OccasionDao) {

    suspend fun insertOccasion(occasion: Occasion){
        occasionDao.insertOccasion(occasion)
    }

    suspend fun updateOccasion(occasion: Occasion){
        occasionDao.updateOccasion(occasion)
    }

    suspend fun deleteOccasion(occasion: Occasion){
        occasionDao.deleteOccasion(occasion)
    }

    fun getOccasionsForSession(idSession: Long): Flow<List<Occasion>>{
        return occasionDao.getOccasionsForSession(idSession)
    }

    fun countOccasionsOfSession(idSession: Long): Flow<Int>{
        return occasionDao.countOccasionsOfSession(idSession)
    }
}