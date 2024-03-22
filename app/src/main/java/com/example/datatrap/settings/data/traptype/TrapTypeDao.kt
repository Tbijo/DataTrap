package com.example.datatrap.settings.data.traptype

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TrapTypeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrapType(trapTypeEntity: TrapTypeEntity)

    @Delete
    suspend fun deleteTrapType(trapTypeEntity: TrapTypeEntity)

    @Query("SELECT * FROM TrapTypeEntity WHERE trapTypeId = :trapTypeId")
    suspend fun getTrapType(trapTypeId: String): TrapTypeEntity

    @Query("SELECT * FROM TrapTypeEntity")
    fun getTrapTypes(): Flow<List<TrapTypeEntity>>
}