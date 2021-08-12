package com.example.datatrap.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.datatrap.databaseio.TrapDatabase
import com.example.datatrap.models.Session
import com.example.datatrap.repositories.SessionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

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

    fun getSessionsForProject(projectName: String): Flow<List<Session>>{
        return sessionRepository.getSessionsForProject(projectName)
    }
}