package com.example.datatrap.settings.envtype.data

import androidx.lifecycle.LiveData

class EnvTypeRepository(
    private val envTypeDao: EnvTypeDao
) {

    val envTypeEntityList: LiveData<List<EnvTypeEntity>> = envTypeDao.getEnvTypes()

    suspend fun insertEnvType(envTypeEntity: EnvTypeEntity) {
        envTypeDao.insertEnvType(envTypeEntity)
    }

    suspend fun updateEnvType(envTypeEntity: EnvTypeEntity) {
        envTypeDao.updateEnvType(envTypeEntity)
    }

    suspend fun deleteEnvType(envTypeEntity: EnvTypeEntity) {
        envTypeDao.deleteEnvType(envTypeEntity)
    }
}