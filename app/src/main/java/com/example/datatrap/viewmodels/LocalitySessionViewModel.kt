package com.example.datatrap.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.datatrap.databaseio.TrapDatabase
import com.example.datatrap.models.localitysession.LocalitySessionCrossRef
import com.example.datatrap.repositories.LocalitySessionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LocalitySessionViewModel(application: Application): AndroidViewModel(application) {

    private val localitySessionRepository: LocalitySessionRepository

    init {
        val localitySessionDao = TrapDatabase.getDatabase(application).localitySessionDao()
        localitySessionRepository = LocalitySessionRepository(localitySessionDao)
    }

    fun insertLocalitySessionCrossRef(localitySessionCrossRef: LocalitySessionCrossRef) {
        viewModelScope.launch(Dispatchers.IO) {
            localitySessionRepository.insertLocalitySessionCrossRef(localitySessionCrossRef)
        }
    }

    fun deleteLocalitySessionCrossRef(localitySessionCrossRef: LocalitySessionCrossRef) {
        viewModelScope.launch(Dispatchers.IO) {
            localitySessionRepository.deleteLocalitySessionCrossRef(localitySessionCrossRef)
        }
    }

    fun existsLocalSessCrossRef(localityId: Long, sessionId: Long): LiveData<Boolean> {
        return localitySessionRepository.existsLocalSessCrossRef(localityId, sessionId)
    }
}