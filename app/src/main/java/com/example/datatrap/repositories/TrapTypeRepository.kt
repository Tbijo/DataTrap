package com.example.datatrap.repositories

import androidx.lifecycle.LiveData
import com.example.datatrap.databaseio.dao.TrapTypeDao
import com.example.datatrap.models.TrapType
import kotlinx.coroutines.flow.Flow

class TrapTypeRepository(private val trapTypeDao: TrapTypeDao) {

    suspend fun insertTrapType(trapType: TrapType){
        trapTypeDao.insertTrapType(trapType)
    }

    suspend fun updateTrapType(trapType: TrapType){
        trapTypeDao.updateTrapType(trapType)
    }

    suspend fun deleteTrapType(trapType: TrapType){
        trapTypeDao.deleteTrapType(trapType)
    }

    val trapTypeList: LiveData<List<TrapType>> = trapTypeDao.getTrapTypes()
}