package com.example.datatrap.settings.vegettype.data

import kotlinx.coroutines.flow.Flow

class VegetTypeRepository(private val vegetTypeDao: VegetTypeDao) {

    fun getVegetTypeEntityList(): Flow<List<VegetTypeEntity>> {
        return vegetTypeDao.getVegetTypes()
    }

    fun getVegetType(vegetTypeId: String): Flow<VegetTypeEntity> {
        return vegetTypeDao.getVegetType(vegetTypeId)
    }

    suspend fun insertVegetType(vegetTypeEntity: VegetTypeEntity) {
        vegetTypeDao.insertVegetType(vegetTypeEntity)
    }

    suspend fun deleteVegetType(vegetTypeEntity: VegetTypeEntity) {
        vegetTypeDao.deleteVegetType(vegetTypeEntity)
    }
}