package com.example.datatrap.settings.envtype.data

import kotlinx.coroutines.flow.Flow

class EnvTypeRepository(
    private val envTypeDao: EnvTypeDao
) {

    val envTypeEntityList: Flow<List<EnvTypeEntity>> = envTypeDao.getEnvTypes()

    suspend fun insertEnvType(envTypeEntity: EnvTypeEntity) {
        envTypeDao.insertEnvType(envTypeEntity)
    }

    suspend fun deleteEnvType(envTypeEntity: EnvTypeEntity) {
        envTypeDao.deleteEnvType(envTypeEntity)
    }
}