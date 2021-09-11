package com.example.datatrap.repositories

import androidx.lifecycle.LiveData
import com.example.datatrap.databaseio.dao.MouseDao
import com.example.datatrap.models.Mouse

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

    fun getMiceForOccasion(idOccasion: Long): LiveData<List<Mouse>>{
        return mouseDao.getMiceForOccasion(idOccasion)
    }

    fun getMiceForCode(code: Int): LiveData<List<Mouse>>{
        return mouseDao.getMiceForCode(code)
    }

    fun getOldMiceForLocality(localityId: Long): LiveData<List<Mouse>>{
        return mouseDao.getOldMiceForLocality(localityId)
    }

    fun searchMice(code: Int): LiveData<List<Mouse>>{
        return mouseDao.searchMice(code)
    }

    fun countMiceForLocality(localityId: Long): LiveData<Int>{
        return mouseDao.countMiceForLocality(localityId)
    }
}