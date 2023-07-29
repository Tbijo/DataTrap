package com.example.datatrap.locality.data

import androidx.lifecycle.LiveData

class LocalityRepository(
    private val localityDao: LocalityDao
) {

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

    suspend fun getLocalityForSync(localityIds: List<Long>): List<Locality> {
        return localityDao.getLocalityForSync(localityIds)
    }

    suspend fun insertSyncLocality(locality: Locality): Long {
        return localityDao.insertSyncLocality(locality)
    }

    suspend fun getLocalityByName(localityName: String): Locality? {
        return localityDao.getLocalityByName(localityName)
    }

}