package com.example.datatrap.settings.traptype.data

import androidx.lifecycle.LiveData

class TrapTypeRepository(private val trapTypeDao: TrapTypeDao) {

    val trapTypeList: LiveData<List<TrapType>> = trapTypeDao.getTrapTypes()

    suspend fun insertTrapType(trapType: TrapType) {
        trapTypeDao.insertTrapType(trapType)
    }

    suspend fun updateTrapType(trapType: TrapType) {
        trapTypeDao.updateTrapType(trapType)
    }

    suspend fun deleteTrapType(trapType: TrapType) {
        trapTypeDao.deleteTrapType(trapType)
    }
}