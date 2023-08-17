package com.example.datatrap.settings.protocol.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ProtocolDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProtocol(protocolEntity: ProtocolEntity)

    @Delete
    suspend fun deleteProtocol(protocolEntity: ProtocolEntity)

    @Query("SELECT * FROM ProtocolEntity")
    fun getProtocols(): Flow<List<ProtocolEntity>>
}