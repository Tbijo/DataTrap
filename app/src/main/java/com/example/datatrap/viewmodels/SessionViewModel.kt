package com.example.datatrap.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.datatrap.databaseio.TrapDatabase
import com.example.datatrap.models.Session
import com.example.datatrap.repositories.SessionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class SessionViewModel(application: Application): AndroidViewModel(application) {

    private val sessionRepository: SessionRepository

    init {
        val sessionDao = TrapDatabase.getDatabase(application).sessionDao()
        sessionRepository = SessionRepository(sessionDao)
    }

    fun insertSession(session: Session){
        viewModelScope.launch(Dispatchers.IO){
            sessionRepository.insertSession(session)
        }
    }

    fun updateSession(session: Session){
        viewModelScope.launch(Dispatchers.IO){
            sessionRepository.updateSession(session)
        }
    }

    fun deleteSession(session: Session){
        viewModelScope.launch(Dispatchers.IO){
            sessionRepository.deleteSession(session)
        }
    }

    fun getSession(sessionId: Long): Session {
        val session: Session
        runBlocking {
            session = sessionRepository.getSession(sessionId)
        }
        return session
    }

    fun getSessionsForProject(projectId: Long): LiveData<List<Session>>{
        return sessionRepository.getSessionsForProject(projectId)
    }
}