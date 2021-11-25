package com.example.datatrap.repositories

import androidx.lifecycle.LiveData
import com.example.datatrap.databaseio.dao.VegetTypeDao
import com.example.datatrap.models.VegetType

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