package com.example.datatrap.settings.data.protocol

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
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