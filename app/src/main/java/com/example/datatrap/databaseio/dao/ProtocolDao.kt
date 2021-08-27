package com.example.datatrap.databaseio.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.datatrap.models.Protocol

@Dao
interface ProtocolDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProtocol(protocol: Protocol)

    @Update
    suspend fun updateProtocol(protocol: Protocol)

    @Delete
    suspend fun deleteProtocol(protocol: Protocol)

    @Query("SELECT * FROM protocols WHERE ProtocolName = :protocolName")
    suspend fun getProtocol(protocolName: String): LiveData<Protocol>

    @Query("SELECT * FROM protocols")
    fun getProtocols(): LiveData<List<Protocol>>
}