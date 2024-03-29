package com.example.datatrap.repositories

import androidx.lifecycle.LiveData
import com.example.datatrap.databaseio.dao.MethodTypeDao
import com.example.datatrap.models.MethodType

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