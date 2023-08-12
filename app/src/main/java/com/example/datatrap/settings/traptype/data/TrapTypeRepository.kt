package com.example.datatrap.settings.traptype.data

import androidx.lifecycle.LiveData

class TrapTypeRepository(private val trapTypeDao: TrapTypeDao) {

    val trapTypeEntityList: LiveData<List<TrapTypeEntity>> = trapTypeDao.getTrapTypes()

    suspend fun insertTrapType(trapTypeEntity: TrapTypeEntity) {
        trapTypeDao.insertTrapType(trapTypeEntity)
    }

    suspend fun updateTrapType(trapTypeEntity: TrapTypeEntity) {
        trapTypeDao.updateTrapType(trapTypeEntity)
    }

    suspend fun deleteTrapType(trapTypeEntity: TrapTypeEntity) {
        trapTypeDao.deleteTrapType(trapTypeEntity)
    }
}