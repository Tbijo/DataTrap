package com.example.datatrap.core.data.locality_session

class LocalitySessionRepository(
    private val localitySessionDao: LocalitySessionDao
) {

    suspend fun insertLocalitySessionCrossRef(localitySessionCrossRef: LocalitySessionCrossRef) {
        localitySessionDao.insertLocalitySessionCrossRef(localitySessionCrossRef)
    }

    suspend fun deleteLocalitySessionCrossRef(localitySessionCrossRef: LocalitySessionCrossRef) {
        localitySessionDao.deleteLocalitySessionCrossRef(localitySessionCrossRef)
    }

    suspend fun existsLocalSessCrossRef(localityId: String, sessionId: String): Boolean {
        return localitySessionDao.existsLocalSessCrossRef(localityId, sessionId)
    }
}