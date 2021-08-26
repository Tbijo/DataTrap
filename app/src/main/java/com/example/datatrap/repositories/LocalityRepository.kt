package com.example.datatrap.repositories

import androidx.lifecycle.LiveData
import com.example.datatrap.databaseio.dao.LocalityDao
import com.example.datatrap.models.Locality

class LocalityRepository(private val localityDao: LocalityDao) {

    suspend fun insertLocality(locality: Locality){
        localityDao.insertLocality(locality)
    }

    suspend fun updateLocality(locality: Locality){
        localityDao.updateLocality(locality)
    }

    suspend fun deleteLocality(locality: Locality){
        localityDao.deleteLocality(locality)
    }

    val localityList: LiveData<List<Locality>> = localityDao.getLocalities()

    fun searchLocalities(localityName: String): LiveData<List<Locality>>{
        return localityDao.searchLocalities(localityName)
    }
}