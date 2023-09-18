package com.example.datatrap.settings.methodtype.data

import kotlinx.coroutines.flow.Flow

class MethodTypeRepository(private val methodTypeDao: MethodTypeDao) {

    fun methodTypeEntityList(): Flow<List<MethodTypeEntity>> {
        return methodTypeDao.getMethodTypes()
    }

    fun getMethodType(methodTypeId: String): Flow<MethodTypeEntity> {
        return methodTypeDao.getMethodType(methodTypeId)
    }

    suspend fun insertMethodType(methodTypeEntity: MethodTypeEntity) {
        methodTypeDao.insertMethodType(methodTypeEntity)
    }

    suspend fun deleteMethodType(methodTypeEntity: MethodTypeEntity) {
        methodTypeDao.deleteMethodType(methodTypeEntity)
    }
}