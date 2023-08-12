package com.example.datatrap.mouse.data

import androidx.lifecycle.LiveData
import com.example.datatrap.mouse.domain.model.MouseLog
import com.example.datatrap.mouse.domain.model.MouseOccList
import com.example.datatrap.mouse.domain.model.MouseRecapList
import com.example.datatrap.mouse.domain.model.MouseView

class MouseRepository(private val mouseDao: MouseDao) {

    suspend fun insertMouse(mouseEntity: MouseEntity): Long {
        return mouseDao.insertMouse(mouseEntity)
    }

    suspend fun updateMouse(mouseEntity: MouseEntity) {
        mouseDao.updateMouse(mouseEntity)
    }

    suspend fun deleteMouse(mouseId: Long) {
        mouseDao.deleteMouse(mouseId)
    }

    fun getMouse(mouseId: Long): LiveData<MouseEntity> {
        return mouseDao.getMouse(mouseId)
    }

    fun getMouseForView(idMouse: Long): LiveData<MouseView> {
        return mouseDao.getMouseForView(idMouse)
    }

    fun getMiceForOccasion(idOccasion: Long): LiveData<List<MouseOccList>> {
        return mouseDao.getMiceForOccasion(idOccasion)
    }

    fun getMiceForRecapture(code: Int?, specieID: Long?, sex: String?, age: String?, gravidity: Boolean, sexActive: Boolean,lactating: Boolean, dateFrom: Long?, dateTo: Long?, currentTime: Long): LiveData<List<MouseRecapList>> {
        return mouseDao.getMiceForRecapture(code, specieID, sex, age, gravidity, sexActive,lactating, dateFrom, dateTo, currentTime)
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

    suspend fun insertMultiMouse(mice: List<MouseEntity>) {
        mouseDao.insertMultiMouse(mice)
    }

    fun getTrapsIdInOccasion(occasionId: Long): LiveData<List<Int>> {
        return mouseDao.getTrapsIdInOccasion(occasionId)
    }

    // SYNC
    suspend fun getMiceForSync(lastSync: Long): List<MouseEntity> {
        return mouseDao.getMiceForSync(lastSync)
    }

    suspend fun inserSynctMouse(mouseEntity: MouseEntity) {
        mouseDao.inserSynctMouse(mouseEntity)
    }

    suspend fun getSyncMouse(occasionID: Long, mouseCaught: Long): MouseEntity? {
        return mouseDao.getSyncMouse(occasionID, mouseCaught)
    }

}