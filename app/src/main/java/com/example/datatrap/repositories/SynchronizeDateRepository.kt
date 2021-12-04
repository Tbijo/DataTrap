package com.example.datatrap.repositories

import androidx.lifecycle.LiveData
import com.example.datatrap.databaseio.dao.SynchronizeDateDao
import com.example.datatrap.models.SynchronizeDate

class SynchronizeDateRepository(private val syncDateDao: SynchronizeDateDao) {

    suspend fun updateLastSyncDate(lastSyncDate: SynchronizeDate) {
        syncDateDao.updateLastSyncDate(lastSyncDate)
    }

    fun getLastUpdateDate(): LiveData<SynchronizeDate> {
        return syncDateDao.getLastUpdateDate()
    }
}