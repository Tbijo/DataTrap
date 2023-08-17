package com.example.datatrap.settings.method.data

import kotlinx.coroutines.flow.Flow

class MethodRepository(
    private val methodDao: MethodDao
) {

    val methodEntityList: Flow<List<MethodEntity>> = methodDao.getMethods()

    suspend fun insertMethod(methodEntity: MethodEntity) {
        methodDao.insertMethod(methodEntity)
    }

    suspend fun deleteMethod(methodEntity: MethodEntity) {
        methodDao.deleteMethod(methodEntity)
    }
}