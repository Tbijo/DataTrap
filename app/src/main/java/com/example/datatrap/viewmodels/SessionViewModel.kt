package com.example.datatrap.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datatrap.models.Session
import com.example.datatrap.repositories.SessionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SessionViewModel @Inject constructor(
    private val sessionRepository: SessionRepository
) : ViewModel() {

    fun insertSession(session: Session) {
        viewModelScope.launch(Dispatchers.IO) {
            sessionRepository.insertSession(session)
        }
    }

    fun updateSession(session: Session) {
        viewModelScope.launch(Dispatchers.IO) {
            sessionRepository.updateSession(session)
        }
    }

    fun deleteSession(session: Session) {
        viewModelScope.launch(Dispatchers.IO) {
            sessionRepository.deleteSession(session)
        }
    }

    fun getSessionsForProject(projectId: Long): LiveData<List<Session>> {
        return sessionRepository.getSessionsForProject(projectId)
    }

    fun getSessionsForEmail(): LiveData<List<Session>> {
        return sessionRepository.getSessionsForEmail()
    }
}