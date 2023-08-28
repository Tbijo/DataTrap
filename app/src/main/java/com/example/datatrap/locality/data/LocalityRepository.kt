package com.example.datatrap.locality.data

import kotlinx.coroutines.flow.Flow

class LocalityRepository(
    private val localityDao: LocalityDao
) {

    fun getLocalities(): Flow<List<LocalityEntity>> {
        return localityDao.getLocalities()
    }

    suspend fun insertLocality(localityEntity: LocalityEntity) {
        localityDao.insertLocality(localityEntity)
    }

    suspend fun deleteLocality(localityEntity: LocalityEntity) {
        localityDao.deleteLocality(localityEntity)
    }

    fun getLocality(localityId: String): Flow<LocalityEntity> {
        return localityDao.getLocality(localityId)
    }

    fun searchLocalities(localityName: String): Flow<List<LocalityEntity>> {
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