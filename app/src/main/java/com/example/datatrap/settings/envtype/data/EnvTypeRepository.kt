package com.example.datatrap.settings.envtype.data

import kotlinx.coroutines.flow.Flow

class EnvTypeRepository(
    private val envTypeDao: EnvTypeDao
) {

    fun getEnvTypeEntityList(): Flow<List<EnvTypeEntity>> {
        return envTypeDao.getEnvTypes()
    }

    suspend fun insertEnvType(envTypeEntity: EnvTypeEntity) {
        envTypeDao.insertEnvType(envTypeEntity)
    }

    suspend fun deleteEnvType(envTypeEntity: EnvTypeEntity) {
        envTypeDao.deleteEnvType(envTypeEntity)
    }
}