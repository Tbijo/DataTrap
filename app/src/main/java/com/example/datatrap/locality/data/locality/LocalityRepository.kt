package com.example.datatrap.locality.data.locality

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

    suspend fun getLocality(localityId: String): LocalityEntity {
        return localityDao.getLocality(localityId)
    }

    fun searchLocalities(localityName: String): Flow<List<LocalityEntity>> {
        return localityDao.searchLocalities(localityName)
    }

}