package com.example.datatrap.mouse.data

import com.example.datatrap.mouse.domain.model.MouseLog
import com.example.datatrap.mouse.domain.model.MouseOccList
import com.example.datatrap.mouse.domain.model.MouseRecapList
import com.example.datatrap.mouse.domain.model.MouseView
import kotlinx.coroutines.flow.Flow

class MouseRepository(private val mouseDao: MouseDao) {

    suspend fun insertMouse(mouseEntity: MouseEntity): String {
        return mouseDao.insertMouse(mouseEntity)
    }

    suspend fun deleteMouse(mouseEntity: MouseEntity) {
        mouseDao.deleteMouse(mouseEntity)
    }

    fun getMouse(mouseId: String): Flow<MouseEntity> {
        return mouseDao.getMouse(mouseId)
    }

    fun getMouseForView(idMouse: String): Flow<MouseView> {
        return mouseDao.getMouseForView(idMouse)
    }

    fun getMiceForOccasion(idOccasion: String): Flow<List<MouseOccList>> {
        return mouseDao.getMiceForOccasion(idOccasion)
    }

    fun getMiceForRecapture(code: Int?, specieID: String?, sex: String?, age: String?, gravidity: Boolean, sexActive: Boolean,lactating: Boolean, dateFrom: Long?, dateTo: Long?, currentTime: Long): Flow<List<MouseRecapList>> {
        return mouseDao.getMiceForRecapture(code, specieID, sex, age, gravidity, sexActive,lactating, dateFrom, dateTo, currentTime)
    }

    fun getMiceForLog(primeMouseID: String, deviceID: String): Flow<List<MouseLog>> {
        return mouseDao.getMiceForLog(primeMouseID, deviceID)
    }

    fun getActiveCodeOfLocality(localityId: String, currentTime: Long): Flow<List<Int>> {
        return mouseDao.getActiveCodeOfLocality(localityId, currentTime)
    }

    fun countMiceForLocality(localityId: String): Flow<Int> {
        return mouseDao.countMiceForLocality(localityId)
    }

    suspend fun insertMultiMouse(mice: List<MouseEntity>) {
        mouseDao.insertMultiMouse(mice)
    }

    fun getTrapsIdInOccasion(occasionId: String): Flow<List<Int>> {
        return mouseDao.getTrapsIdInOccasion(occasionId)
    }

    // SYNC
    suspend fun getMiceForSync(lastSync: Long): List<MouseEntity> {
        return mouseDao.getMiceForSync(lastSync)
    }

    suspend fun inserSynctMouse(mouseEntity: MouseEntity) {
        mouseDao.inserSynctMouse(mouseEntity)
    }

    suspend fun getSyncMouse(occasionID: String, mouseCaught: Long): MouseEntity? {
        return mouseDao.getSyncMouse(occasionID, mouseCaught)
    }

}