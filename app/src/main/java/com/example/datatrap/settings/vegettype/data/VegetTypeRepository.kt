package com.example.datatrap.settings.vegettype.data

import androidx.lifecycle.LiveData

class VegetTypeRepository(private val vegetTypeDao: VegetTypeDao) {

    val vegetTypeEntityList: LiveData<List<VegetTypeEntity>> = vegetTypeDao.getVegetTypes()

    suspend fun insertVegetType(vegetTypeEntity: VegetTypeEntity) {
        vegetTypeDao.insertVegetType(vegetTypeEntity)
    }

    suspend fun updateVegetType(vegetTypeEntity: VegetTypeEntity) {
        vegetTypeDao.updateVegetType(vegetTypeEntity)
    }

    suspend fun deleteVegetType(vegetTypeEntity: VegetTypeEntity) {
        vegetTypeDao.deleteVegetType(vegetTypeEntity)
    }
}