package com.example.datatrap.databaseio.dao

import androidx.room.*
import com.example.datatrap.models.Protocol
import kotlinx.coroutines.flow.Flow

@Dao
interface ProtocolDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertProtocol(protocol: Protocol)

    @Update
    suspend fun updateProtocol(protocol: Protocol)

    @Delete
    suspend fun deleteProtocol(protocol: Protocol)

    @Query("SELECT * FROM protocols WHERE ProtocolName = :protocolName")
    suspend fun getProtocol(protocolName: String): Protocol

    @Query("SELECT * FROM protocols")
    fun getProtocols(): Flow<List<Protocol>>
}