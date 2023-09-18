package com.example.datatrap.settings.traptype.data

import kotlinx.coroutines.flow.Flow

class TrapTypeRepository(private val trapTypeDao: TrapTypeDao) {

    fun getTrapTypeEntityList(): Flow<List<TrapTypeEntity>> {
        return trapTypeDao.getTrapTypes()
    }

    fun getTrapType(trapTypeId: String): Flow<TrapTypeEntity> {
        return trapTypeDao.getTrapType(trapTypeId)
    }

    suspend fun insertTrapType(trapTypeEntity: TrapTypeEntity) {
        trapTypeDao.insertTrapType(trapTypeEntity)
    }

    suspend fun deleteTrapType(trapTypeEntity: TrapTypeEntity) {
        trapTypeDao.deleteTrapType(trapTypeEntity)
    }
}