package com.example.datatrap.settings.vegettype.data

import androidx.lifecycle.LiveData

class VegetTypeRepository(private val vegetTypeDao: VegetTypeDao) {

    val vegetTypeList: LiveData<List<VegetType>> = vegetTypeDao.getVegetTypes()

    suspend fun insertVegetType(vegetType: VegetType) {
        vegetTypeDao.insertVegetType(vegetType)
    }

    suspend fun updateVegetType(vegetType: VegetType) {
        vegetTypeDao.updateVegetType(vegetType)
    }

    suspend fun deleteVegetType(vegetType: VegetType) {
        vegetTypeDao.deleteVegetType(vegetType)
    }
}