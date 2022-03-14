package com.example.datatrap.repositories

import androidx.lifecycle.LiveData
import com.example.datatrap.databaseio.dao.EnvTypeDao
import com.example.datatrap.models.EnvType

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