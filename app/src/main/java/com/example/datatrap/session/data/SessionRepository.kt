package com.example.datatrap.session.data

import androidx.lifecycle.LiveData

class SessionRepository(private val sessionDao: SessionDao) {

    suspend fun insertSession(sessionEntity: SessionEntity) {
        sessionDao.insertSession(sessionEntity)
    }

    suspend fun updateSession(sessionEntity: SessionEntity) {
        sessionDao.updateSession(sessionEntity)
    }

    suspend fun deleteSession(sessionEntity: SessionEntity) {
        sessionDao.deleteSession(sessionEntity)
    }

    fun getSessionsForProject(projectId: Long): LiveData<List<SessionEntity>> {
        return sessionDao.getSessionsForProject(projectId)
    }

    suspend fun getSessionForSync(sessionIds: List<Long>): List<SessionEntity> {
        return sessionDao.getSessionForSync(sessionIds)
    }

    suspend fun insertSyncSession(sessionEntity: SessionEntity): Long {
        return sessionDao.insertSyncSession(sessionEntity)
    }

    suspend fun getSession(projectID: Long, sessionStart: Long): SessionEntity? {
        return sessionDao.getSession(projectID, sessionStart)
    }

    // 604800 dlzka tyzdna v sekundach treba v milisekundach 604 800 000
    suspend fun getNearSession(projectID: Long, sessionStart: Long): SessionEntity? {
        return sessionDao.getNearSession(projectID, sessionStart)
    }

}