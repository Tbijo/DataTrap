package com.example.datatrap.settings.vegettype.data

import kotlinx.coroutines.flow.Flow

class VegetTypeRepository(private val vegetTypeDao: VegetTypeDao) {

    val vegetTypeEntityList: Flow<List<VegetTypeEntity>> = vegetTypeDao.getVegetTypes()

    suspend fun insertVegetType(vegetTypeEntity: VegetTypeEntity) {
        vegetTypeDao.insertVegetType(vegetTypeEntity)
    }

    suspend fun deleteVegetType(vegetTypeEntity: VegetTypeEntity) {
        vegetTypeDao.deleteVegetType(vegetTypeEntity)
    }
}