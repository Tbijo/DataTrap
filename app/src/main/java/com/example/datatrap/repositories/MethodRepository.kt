package com.example.datatrap.repositories

import androidx.lifecycle.LiveData
import com.example.datatrap.databaseio.dao.MethodDao
import com.example.datatrap.models.Method

class MethodRepository(private val methodDao: MethodDao) {

    val methodList: LiveData<List<Method>> = methodDao.getMethods()

    suspend fun insertMethod(method: Method) {
        methodDao.insertMethod(method)
    }

    suspend fun updateMethod(method: Method) {
        methodDao.updateMethod(method)
    }

    suspend fun deleteMethod(method: Method) {
        methodDao.deleteMethod(method)
    }
}