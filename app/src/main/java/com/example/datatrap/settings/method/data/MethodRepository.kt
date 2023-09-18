package com.example.datatrap.settings.method.data

import kotlinx.coroutines.flow.Flow

class MethodRepository(
    private val methodDao: MethodDao
) {
    fun methodEntityList(): Flow<List<MethodEntity>> {
        return methodDao.getMethods()
    }

    fun getMethod(methodId: String): Flow<MethodEntity> {
        return methodDao.getMethod(methodId)
    }

    suspend fun insertMethod(methodEntity: MethodEntity) {
        methodDao.insertMethod(methodEntity)
    }

    suspend fun deleteMethod(methodEntity: MethodEntity) {
        methodDao.deleteMethod(methodEntity)
    }
}