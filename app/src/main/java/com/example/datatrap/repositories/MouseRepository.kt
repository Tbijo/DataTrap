package com.example.datatrap.repositories

import com.example.datatrap.databaseio.dao.MouseDao
import com.example.datatrap.models.Mouse
import kotlinx.coroutines.flow.Flow

class MouseRepository(private val mouseDao: MouseDao) {

    suspend fun insertMouse(mouse: Mouse){
        mouseDao.insertMouse(mouse)
    }

    suspend fun updateMouse(mouse: Mouse){
        mouseDao.updateMouse(mouse)
    }

    suspend fun deleteMouse(mouse: Mouse){
        mouseDao.deleteMouse(mouse)
    }

    fun getMiceForOccasion(idOccasion: Int): Flow<List<Mouse>>{
        return mouseDao.getMiceForOccasion(idOccasion)
    }
}