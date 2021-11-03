package com.example.datatrap.repositories

import androidx.lifecycle.LiveData
import com.example.datatrap.databaseio.dao.SessionDao
import com.example.datatrap.models.Session

class SessionRepository(private val sessionDao: SessionDao) {

    suspend fun insertSession(session: Session){
        sessionDao.insertSession(session)
    }

    suspend fun updateSession(session: Session){
        sessionDao.updateSession(session)
    }

    suspend fun deleteSession(session: Session){
        sessionDao.deleteSession(session)
    }

    suspend fun getSession(sessionId: Long): Session {
        return sessionDao.getSession(sessionId)
    }

    fun getSessionsForProject(projectId: Long): LiveData<List<Session>>{
        return sessionDao.getSessionsForProject(projectId)
    }
}