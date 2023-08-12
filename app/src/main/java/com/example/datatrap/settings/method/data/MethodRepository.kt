package com.example.datatrap.settings.method.data

import androidx.lifecycle.LiveData

class MethodRepository(
    private val methodDao: MethodDao
) {

    val methodEntityList: LiveData<List<MethodEntity>> = methodDao.getMethods()

    suspend fun insertMethod(methodEntity: MethodEntity) {
        methodDao.insertMethod(methodEntity)
    }

    suspend fun updateMethod(methodEntity: MethodEntity) {
        methodDao.updateMethod(methodEntity)
    }

    suspend fun deleteMethod(methodEntity: MethodEntity) {
        methodDao.deleteMethod(methodEntity)
    }
}