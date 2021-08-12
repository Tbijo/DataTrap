package com.example.datatrap.repositories

import com.example.datatrap.databaseio.dao.EnvTypeDao
import com.example.datatrap.models.EnvType
import kotlinx.coroutines.flow.Flow

class EnvTypeRepository(private val envTypeDao: EnvTypeDao) {

    suspend fun insertEnvType(envType: EnvType){
        envTypeDao.insertEnvType(envType)
    }

    suspend fun updateEnvType(envType: EnvType){
        envTypeDao.updateEnvType(envType)
    }

    suspend fun deleteEnvType(envType: EnvType){
        envTypeDao.deleteEnvType(envType)
    }

    val envTypeList: Flow<List<EnvType>> = envTypeDao.getEnvTypes()
}