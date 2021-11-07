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

    suspend fun getMouse(idMouse: Long, deviceID: String): Mouse {
        return mouseDao.getMouse(idMouse, deviceID)
    }

    fun getMiceForOccasion(idOccasion: Long): LiveData<List<Mouse>>{
        return mouseDao.getMiceForOccasion(idOccasion)
    }

    fun getMiceForRecapture(code: Int): LiveData<List<Mouse>>{
        return mouseDao.getMiceForRecapture(code)
    }

    fun getMiceForLog(idMouse: Long, deviceID: String): LiveData<List<Mouse>>{
        return mouseDao.getMiceForLog(idMouse, deviceID)
    }

    fun getActiveMiceOfLocality(localityId: Long, currentTime: Long, twoYears: Long): LiveData<List<Mouse>>{
        return mouseDao.getActiveMiceOfLocality(localityId, currentTime, twoYears)
    }

    fun countMiceForLocality(localityId: Long): LiveData<Int> {
        return mouseDao.countMiceForLocality(localityId)
    }
}