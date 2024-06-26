package com.example.datatrap.mouse.data

import kotlinx.coroutines.flow.Flow

class MouseRepository(private val mouseDao: MouseDao) {

    suspend fun insertMouse(mouseEntity: MouseEntity) {
        mouseDao.insertMouse(mouseEntity)
    }

    suspend fun insertMice(mice: List<MouseEntity>) {
        mouseDao.insertMice(mice)
    }

    suspend fun deleteMouse(mouseEntity: MouseEntity) {
        mouseDao.deleteMouse(mouseEntity)
    }

    suspend fun getMouse(mouseId: String): MouseEntity {
        return mouseDao.getMouse(mouseId)
    }

    suspend fun getMice(): List<MouseEntity> {
        return mouseDao.getMice()
    }

    fun getMiceForOccasion(occasionID: String): Flow<List<MouseEntity>> {
        return mouseDao.getMiceForOccasion(occasionID)
    }

    suspend fun getMiceByPrimeMouseID(primeMouseID: String): List<MouseEntity> {
        return mouseDao.getMiceByPrimeMouseID(primeMouseID)
    }

}