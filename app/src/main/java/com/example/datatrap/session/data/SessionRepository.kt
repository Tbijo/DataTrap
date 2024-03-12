package com.example.datatrap.session.data

import kotlinx.coroutines.flow.Flow

class SessionRepository(private val sessionDao: SessionDao) {

    suspend fun insertSession(sessionEntity: SessionEntity) {
        sessionDao.insertSession(sessionEntity)
    }

    suspend fun deleteSession(sessionEntity: SessionEntity) {
        sessionDao.deleteSession(sessionEntity)
    }

    suspend fun getSession(sessionId: String): SessionEntity {
        return sessionDao.getSession(sessionId)
    }

    fun getSessionsForProject(projectId: String): Flow<List<SessionEntity>> {
        return sessionDao.getSessionsForProject(projectId)
    }

}