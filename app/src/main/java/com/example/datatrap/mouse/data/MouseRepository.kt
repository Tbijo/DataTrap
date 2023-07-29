package com.example.datatrap.mouse.data

import androidx.lifecycle.LiveData

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

    suspend fun insertMultiMouse(mice: List<Mouse>) {
        mouseDao.insertMultiMouse(mice)
    }

    fun getTrapsIdInOccasion(occasionId: Long): LiveData<List<Int>> {
        return mouseDao.getTrapsIdInOccasion(occasionId)
    }

    // SYNC
    suspend fun getMiceForSync(lastSync: Long): List<Mouse> {
        return mouseDao.getMiceForSync(lastSync)
    }

    suspend fun inserSynctMouse(mouse: Mouse) {
        mouseDao.inserSynctMouse(mouse)
    }

    suspend fun getSyncMouse(occasionID: Long, mouseCaught: Long): Mouse? {
        return mouseDao.getSyncMouse(occasionID, mouseCaught)
    }

}