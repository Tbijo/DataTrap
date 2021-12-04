package com.example.datatrap.databaseio.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import com.example.datatrap.models.SynchronizeDate

@Dao
interface SynchronizeDateDao {

    @Update
    suspend fun updateLastSyncDate(lastSyncDate: SynchronizeDate)

    @Query("SELECT * FROM SynchronizeDate WHERE lastSyncId = 1")
    fun getLastUpdateDate(): LiveData<SynchronizeDate>
}