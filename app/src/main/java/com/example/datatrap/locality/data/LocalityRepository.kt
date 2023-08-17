package com.example.datatrap.locality.data

import com.example.datatrap.locality.domain.model.LocList
import kotlinx.coroutines.flow.Flow

class LocalityRepository(
    private val localityDao: LocalityDao
) {

    val localityList: Flow<List<LocList>> = localityDao.getLocalities()

    suspend fun insertLocality(localityEntity: LocalityEntity) {
        localityDao.insertLocality(localityEntity)
    }

    suspend fun deleteLocality(localityId: Long) {
        localityDao.deleteLocality(localityId)
    }

    fun getLocality(localityId: Long): Flow<LocalityEntity> {
        return localityDao.getLocality(localityId)
    }

    fun searchLocalities(localityName: String): Flow<List<LocList>> {
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