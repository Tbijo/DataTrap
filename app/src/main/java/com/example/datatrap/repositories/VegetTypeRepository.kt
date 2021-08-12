package com.example.datatrap.repositories

import com.example.datatrap.databaseio.dao.VegetTypeDao
import com.example.datatrap.models.VegetType
import kotlinx.coroutines.flow.Flow

class VegetTypeRepository(private val vegetTypeDao: VegetTypeDao) {

    suspend fun insertVegetType(vegetType: VegetType){
        vegetTypeDao.insertVegetType(vegetType)
    }

    suspend fun updateVegetType(vegetType: VegetType){
        vegetTypeDao.updateVegetType(vegetType)
    }

    suspend fun deleteVegetType(vegetType: VegetType){
        vegetTypeDao.deleteVegetType(vegetType)
    }

    val vegetTypeList: Flow<List<VegetType>> = vegetTypeDao.getVegetTypes()
}