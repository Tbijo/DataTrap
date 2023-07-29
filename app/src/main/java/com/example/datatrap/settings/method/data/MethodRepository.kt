package com.example.datatrap.settings.method.data

import androidx.lifecycle.LiveData

class MethodRepository(
    private val methodDao: MethodDao
) {

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