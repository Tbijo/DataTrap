package com.example.datatrap.settings.protocol.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ProtocolDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProtocol(protocol: Protocol)

    @Update
    suspend fun updateProtocol(protocol: Protocol)

    @Delete
    suspend fun deleteProtocol(protocol: Protocol)

    @Query("SELECT * FROM Protocol")
    fun getProtocols(): LiveData<List<Protocol>>
}