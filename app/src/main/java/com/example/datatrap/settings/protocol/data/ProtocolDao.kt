package com.example.datatrap.settings.protocol.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ProtocolDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProtocol(protocolEntity: ProtocolEntity)

    @Update
    suspend fun updateProtocol(protocolEntity: ProtocolEntity)

    @Delete
    suspend fun deleteProtocol(protocolEntity: ProtocolEntity)

    @Query("SELECT * FROM ProtocolEntity")
    fun getProtocols(): LiveData<List<ProtocolEntity>>
}