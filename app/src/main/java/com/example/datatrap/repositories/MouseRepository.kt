package com.example.datatrap.repositories

import androidx.lifecycle.LiveData
import com.example.datatrap.databaseio.dao.MouseDao
import com.example.datatrap.models.Mouse
import com.example.datatrap.models.tuples.MouseLog
import com.example.datatrap.models.tuples.MouseOccList
import com.example.datatrap.models.tuples.MouseRecapList
import com.example.datatrap.models.tuples.MouseView

class MouseRepository(private val mouseDao: MouseDao) {

    suspend fun insertMouse(mouse: Mouse): Long {
        return mouseDao.insertMouse(mouse)
    }

    suspend fun updateMouse(mouse: Mouse) {
        mouseDao.updateMouse(mouse)
    }

    suspend fun deleteMouse(mouseId: Long) {
        mouseDao.deleteMouse(mouseId)
    }

    fun getMouse(mouseId: Long): LiveData<Mouse> {
        return mouseDao.getMouse(mouseId)
    }

    fun getMouseForView(idMouse: Long, deviceID: String): LiveData<MouseView> {
        return mouseDao.getMouseForView(idMouse, deviceID)
    }

    fun getMiceForOccasion(idOccasion: Long): LiveData<List<MouseOccList>> {
        return mouseDao.getMiceForOccasion(idOccasion)
    }

    fun getMiceForRecapture(code: Int, specieID: Long, sex: String?, age: String?, gravidity: Boolean, sexActive: Boolean,lactating: Boolean, dateFrom: Long, dateTo: Long): LiveData<List<MouseRecapList>> {
        return mouseDao.getMiceForRecapture(code, specieID, sex, age, gravidity, sexActive,lactating, dateFrom, dateTo)
    }

    fun getMiceForLog(primeMouseID: Long, deviceID: String): LiveData<List<MouseLog>> {
        return mouseDao.getMiceForLog(primeMouseID, deviceID)
    }

    fun getActiveCodeOfLocality(localityId: Long, currentTime: Long): LiveData<List<Int>> {
        return mouseDao.getActiveCodeOfLocality(localityId, currentTime)
    }

    fun countMiceForLocality(localityId: Long): LiveData<Int> {
        return mouseDao.countMiceForLocality(localityId)
    }

    fun getMiceForEmail(): LiveData<List<Mouse>> {
        return mouseDao.getMiceForEmail()
    }

    suspend fun insertMultiMouse(mice: List<Mouse>) {
        mouseDao.insertMultiMouse(mice)
    }

    fun getTrapsIdInOccasion(occasionId: Long): LiveData<List<Int>> {
        return mouseDao.getTrapsIdInOccasion(occasionId)
    }
}