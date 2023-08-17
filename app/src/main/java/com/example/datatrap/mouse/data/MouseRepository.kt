package com.example.datatrap.mouse.data

import com.example.datatrap.mouse.domain.model.MouseLog
import com.example.datatrap.mouse.domain.model.MouseOccList
import com.example.datatrap.mouse.domain.model.MouseRecapList
import com.example.datatrap.mouse.domain.model.MouseView
import kotlinx.coroutines.flow.Flow

class MouseRepository(private val mouseDao: MouseDao) {

    suspend fun insertMouse(mouseEntity: MouseEntity): Long {
        return mouseDao.insertMouse(mouseEntity)
    }

    suspend fun deleteMouse(mouseId: Long) {
        mouseDao.deleteMouse(mouseId)
    }

    fun getMouse(mouseId: Long): Flow<MouseEntity> {
        return mouseDao.getMouse(mouseId)
    }

    fun getMouseForView(idMouse: Long): Flow<MouseView> {
        return mouseDao.getMouseForView(idMouse)
    }

    fun getMiceForOccasion(idOccasion: Long): Flow<List<MouseOccList>> {
        return mouseDao.getMiceForOccasion(idOccasion)
    }

    fun getMiceForRecapture(code: Int?, specieID: Long?, sex: String?, age: String?, gravidity: Boolean, sexActive: Boolean,lactating: Boolean, dateFrom: Long?, dateTo: Long?, currentTime: Long): Flow<List<MouseRecapList>> {
        return mouseDao.getMiceForRecapture(code, specieID, sex, age, gravidity, sexActive,lactating, dateFrom, dateTo, currentTime)
    }

    fun getMiceForLog(primeMouseID: Long, deviceID: String): Flow<List<MouseLog>> {
        return mouseDao.getMiceForLog(primeMouseID, deviceID)
    }

    fun getActiveCodeOfLocality(localityId: Long, currentTime: Long): Flow<List<Int>> {
        return mouseDao.getActiveCodeOfLocality(localityId, currentTime)
    }

    fun countMiceForLocality(localityId: Long): Flow<Int> {
        return mouseDao.countMiceForLocality(localityId)
    }

    suspend fun insertMultiMouse(mice: List<MouseEntity>) {
        mouseDao.insertMultiMouse(mice)
    }

    fun getTrapsIdInOccasion(occasionId: Long): Flow<List<Int>> {
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