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

    suspend fun getSessionForSync(sessionIds: List<String>): List<SessionEntity> {
        return sessionDao.getSessionForSync(sessionIds)
    }

    suspend fun insertSyncSession(sessionEntity: SessionEntity): Long {
        return sessionDao.insertSyncSession(sessionEntity)
    }

    // 604 800 dlzka tyzdna v sekundach treba v milisekundach 604 800 000
//    suspend fun getNearSession(projectID: Long, sessionStart: Long): SessionEntity? {
//        return sessionDao.getNearSession(projectID, sessionStart)
//    }

}