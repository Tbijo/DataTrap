package com.example.datatrap.repositories

import androidx.lifecycle.LiveData
import com.example.datatrap.databaseio.dao.MethodDao
import com.example.datatrap.models.Method
import kotlinx.coroutines.flow.Flow

class MethodRepository(private val methodDao: MethodDao) {

    suspend fun insertMethod(method: Method){
        methodDao.insertMethod(method)
    }

    suspend fun updateMethod(method: Method){
        methodDao.updateMethod(method)
    }

    suspend fun deleteMethod(method: Method){
        methodDao.deleteMethod(method)
    }

    val methodList: LiveData<List<Method>> = methodDao.getMethods()
}