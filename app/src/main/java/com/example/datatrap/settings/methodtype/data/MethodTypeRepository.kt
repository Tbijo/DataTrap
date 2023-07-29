package com.example.datatrap.settings.methodtype.data

import androidx.lifecycle.LiveData

class MethodTypeRepository(private val methodTypeDao: MethodTypeDao) {

    val methodTypeList: LiveData<List<MethodType>> = methodTypeDao.getMethodTypes()

    suspend fun insertMethodType(methodType: MethodType) {
        methodTypeDao.insertMethodType(methodType)
    }

    suspend fun updateMethodType(methodType: MethodType) {
        methodTypeDao.updateMethodType(methodType)
    }

    suspend fun deleteMethodType(methodType: MethodType) {
        methodTypeDao.deleteMethodType(methodType)
    }
}