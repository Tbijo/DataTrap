package com.example.datatrap.repositories

import androidx.lifecycle.LiveData
import com.example.datatrap.databaseio.dao.LocalityDao
import com.example.datatrap.models.Locality
import com.example.datatrap.models.tuples.LocList

class LocalityRepository(private val localityDao: LocalityDao) {

    val localityList: LiveData<List<LocList>> = localityDao.getLocalities()

    suspend fun insertLocality(locality: Locality) {
        localityDao.insertLocality(locality)
    }

    suspend fun updateLocality(locality: Locality) {
        localityDao.updateLocality(locality)
    }

    suspend fun deleteLocality(localityId: Long) {
        localityDao.deleteLocality(localityId)
    }

    fun getLocality(localityId: Long): LiveData<Locality> {
        return localityDao.getLocality(localityId)
    }

    fun searchLocalities(localityName: String): LiveData<List<LocList>> {
        return localityDao.searchLocalities(localityName)
    }
}