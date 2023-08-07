package com.example.datatrap.locality

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datatrap.locality.data.LocalitySessionCrossRef
import com.example.datatrap.locality.data.LocalitySessionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocalitySessionViewModel @Inject constructor (
    private val localitySessionRepository: LocalitySessionRepository
): ViewModel() {

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