package com.example.datatrap.settings.methodtype.data

import kotlinx.coroutines.flow.Flow

class MethodTypeRepository(private val methodTypeDao: MethodTypeDao) {

    val methodTypeEntityList: Flow<List<MethodTypeEntity>> = methodTypeDao.getMethodTypes()

    suspend fun insertMethodType(methodTypeEntity: MethodTypeEntity) {
        methodTypeDao.insertMethodType(methodTypeEntity)
    }

    suspend fun deleteMethodType(methodTypeEntity: MethodTypeEntity) {
        methodTypeDao.deleteMethodType(methodTypeEntity)
    }
}