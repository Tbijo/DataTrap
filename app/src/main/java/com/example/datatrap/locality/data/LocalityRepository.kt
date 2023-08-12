package com.example.datatrap.locality.data

import androidx.lifecycle.LiveData
import com.example.datatrap.locality.domain.model.LocList

class LocalityRepository(
    private val localityDao: LocalityDao
) {

    val localityList: LiveData<List<LocList>> = localityDao.getLocalities()

    suspend fun insertLocality(localityEntity: LocalityEntity) {
        localityDao.insertLocality(localityEntity)
    }

    suspend fun updateLocality(localityEntity: LocalityEntity) {
        localityDao.updateLocality(localityEntity)
    }

    suspend fun deleteLocality(localityId: Long) {
        localityDao.deleteLocality(localityId)
    }

    fun getLocality(localityId: Long): LiveData<LocalityEntity> {
        return localityDao.getLocality(localityId)
    }

    fun searchLocalities(localityName: String): LiveData<List<LocList>> {
        return localityDao.searchLocalities(localityName)
    }

    suspend fun getLocalityForSync(localityIds: List<Long>): List<LocalityEntity> {
        return localityDao.getLocalityForSync(localityIds)
    }

    suspend fun insertSyncLocality(localityEntity: LocalityEntity): Long {
        return localityDao.insertSyncLocality(localityEntity)
    }

    suspend fun getLocalityByName(localityName: String): LocalityEntity? {
        return localityDao.getLocalityByName(localityName)
    }

}