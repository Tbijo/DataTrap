package com.example.datatrap.repositories

import androidx.lifecycle.LiveData
import androidx.room.Query
import com.example.datatrap.databaseio.dao.SessionDao
import com.example.datatrap.models.Session
import com.example.datatrap.models.sync.SessionSync

class SessionRepository(private val sessionDao: SessionDao) {

    suspend fun insertSession(session: Session) {
        sessionDao.insertSession(session)
    }

    suspend fun updateSession(session: Session) {
        sessionDao.updateSession(session)
    }

    suspend fun deleteSession(session: Session) {
        sessionDao.deleteSession(session)
    }

    fun getSessionsForProject(projectId: Long): LiveData<List<Session>> {
        return sessionDao.getSessionsForProject(projectId)
    }

    suspend fun getSessionForSync(sessionIds: List<Long>): List<Session> {
        return sessionDao.getSessionForSync(sessionIds)
    }

    suspend fun insertSyncSession(session: Session): Long {
        return sessionDao.insertSyncSession(session)
    }

    suspend fun getSession(projectID: Long, sessionStart: Long): Session? {
        return sessionDao.getSession(projectID, sessionStart)
    }

    // 604800 dlzka tyzdna v sekundach treba v milisekundach 604 800 000
    suspend fun getNearSession(projectID: Long, sessionStart: Long): Session? {
        return sessionDao.getNearSession(projectID, sessionStart)
    }

}