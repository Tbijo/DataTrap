package com.example.datatrap.settings.vegettype.data

import kotlinx.coroutines.flow.Flow

class VegetTypeRepository(private val vegetTypeDao: VegetTypeDao) {

    fun getVegetTypeEntityList(): Flow<List<VegetTypeEntity>> {
        return vegetTypeDao.getVegetTypes()
    }

    suspend fun insertVegetType(vegetTypeEntity: VegetTypeEntity) {
        vegetTypeDao.insertVegetType(vegetTypeEntity)
    }

    suspend fun deleteVegetType(vegetTypeEntity: VegetTypeEntity) {
        vegetTypeDao.deleteVegetType(vegetTypeEntity)
    }
}