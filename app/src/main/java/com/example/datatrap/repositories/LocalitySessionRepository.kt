package com.example.datatrap.repositories

import androidx.lifecycle.LiveData
import com.example.datatrap.databaseio.dao.LocalitySessionDao
import com.example.datatrap.models.localitysession.LocalitySessionCrossRef

class LocalitySessionRepository(private val localitySessionDao: LocalitySessionDao) {

    suspend fun insertLocalitySessionCrossRef(localitySessionCrossRef: LocalitySessionCrossRef) {
        localitySessionDao.insertLocalitySessionCrossRef(localitySessionCrossRef)
    }

    suspend fun deleteLocalitySessionCrossRef(localitySessionCrossRef: LocalitySessionCrossRef) {
        localitySessionDao.deleteLocalitySessionCrossRef(localitySessionCrossRef)
    }

    fun existsLocalSessCrossRef(localityId: Long, sessionId: Long): LiveData<Boolean> {
        return localitySessionDao.existsLocalSessCrossRef(localityId, sessionId)
    }
}