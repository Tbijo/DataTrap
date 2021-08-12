package com.example.datatrap.repositories

import com.example.datatrap.databaseio.dao.SessionDao
import com.example.datatrap.models.Session
import kotlinx.coroutines.flow.Flow

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

    fun getSessionsForProject(projectName: String): Flow<List<Session>>{
        return sessionDao.getSessionsForProject(projectName)
    }
}