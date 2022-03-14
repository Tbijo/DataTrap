package com.example.datatrap.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datatrap.models.SynchronizeDate
import com.example.datatrap.repositories.SynchronizeDateRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SynchronizeDateViewModel @Inject constructor(
    private val synchronizeDateRepository: SynchronizeDateRepository
): ViewModel() {

    fun updateLastSyncDate(lastSyncDate: SynchronizeDate) {
        viewModelScope.launch(Dispatchers.IO) {
            synchronizeDateRepository.updateLastSyncDate(lastSyncDate)
        }
    }

    fun getLastUpdateDate(): LiveData<SynchronizeDate> {
        return synchronizeDateRepository.getLastUpdateDate()
    }
}