package com.example.datatrap.session.data

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

    suspend fun getSessionsForProject(projectId: String): List<SessionEntity> {
        return sessionDao.getSessionsForProject(projectId)
    }

}