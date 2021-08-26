package com.example.datatrap.repositories

import androidx.lifecycle.LiveData
import com.example.datatrap.databaseio.dao.OccasionDao
import com.example.datatrap.models.Occasion

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

    fun getOccasionsForSession(idSession: Long): LiveData<List<Occasion>>{
        return occasionDao.getOccasionsForSession(idSession)
    }

    fun countOccasionsOfSession(idSession: Long): LiveData<Int>{
        return occasionDao.countOccasionsOfSession(idSession)
    }
}