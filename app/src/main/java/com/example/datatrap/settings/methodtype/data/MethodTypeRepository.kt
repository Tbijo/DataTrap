package com.example.datatrap.settings.methodtype.data

import androidx.lifecycle.LiveData

class MethodTypeRepository(private val methodTypeDao: MethodTypeDao) {

    val methodTypeEntityList: LiveData<List<MethodTypeEntity>> = methodTypeDao.getMethodTypes()

    suspend fun insertMethodType(methodTypeEntity: MethodTypeEntity) {
        methodTypeDao.insertMethodType(methodTypeEntity)
    }

    suspend fun updateMethodType(methodTypeEntity: MethodTypeEntity) {
        methodTypeDao.updateMethodType(methodTypeEntity)
    }

    suspend fun deleteMethodType(methodTypeEntity: MethodTypeEntity) {
        methodTypeDao.deleteMethodType(methodTypeEntity)
    }
}