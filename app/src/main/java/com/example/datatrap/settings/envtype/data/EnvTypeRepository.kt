package com.example.datatrap.settings.envtype.data

import androidx.lifecycle.LiveData

class EnvTypeRepository(
    private val envTypeDao: EnvTypeDao
) {

    val envTypeList: LiveData<List<EnvType>> = envTypeDao.getEnvTypes()

    suspend fun insertEnvType(envType: EnvType) {
        envTypeDao.insertEnvType(envType)
    }

    suspend fun updateEnvType(envType: EnvType) {
        envTypeDao.updateEnvType(envType)
    }

    suspend fun deleteEnvType(envType: EnvType) {
        envTypeDao.deleteEnvType(envType)
    }
}