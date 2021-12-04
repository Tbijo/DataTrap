package com.example.datatrap.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.datatrap.databaseio.TrapDatabase
import com.example.datatrap.models.SynchronizeDate
import com.example.datatrap.repositories.SynchronizeDateRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SynchronizeDateViewModel(application: Application): AndroidViewModel(application) {

    private val synchronizeDateRepository: SynchronizeDateRepository

    init {
        val synchronizeDateDao = TrapDatabase.getDatabase(application).synchronizeDao()
        synchronizeDateRepository = SynchronizeDateRepository(synchronizeDateDao)
    }

    fun updateLastSyncDate(lastSyncDate: SynchronizeDate) {
        viewModelScope.launch(Dispatchers.IO) {
            synchronizeDateRepository.updateLastSyncDate(lastSyncDate)
        }
    }

    fun getLastUpdateDate(): LiveData<SynchronizeDate> {
        return synchronizeDateRepository.getLastUpdateDate()
    }
}