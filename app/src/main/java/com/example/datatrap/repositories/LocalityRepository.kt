package com.example.datatrap.repositories

import androidx.lifecycle.LiveData
import com.example.datatrap.databaseio.dao.LocalityDao
import com.example.datatrap.models.Locality

class LocalityRepository(private val localityDao: LocalityDao) {

    val localityList: LiveData<List<Locality>> = localityDao.getLocalities()

    suspend fun insertLocality(locality: Locality){
        localityDao.insertLocality(locality)
    }

    suspend fun updateLocality(locality: Locality){
        localityDao.updateLocality(locality)
    }

    suspend fun deleteLocality(locality: Locality){
        localityDao.deleteLocality(locality)
    }

    suspend fun getLocality(localityId: Long): Locality? {
        return localityDao.getLocality(localityId)
    }

    fun searchLocalities(localityName: String): LiveData<List<Locality>>{
        return localityDao.searchLocalities(localityName)
    }
}