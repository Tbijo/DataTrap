package com.example.datatrap.settings.traptype.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TrapTypeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrapType(trapTypeEntity: TrapTypeEntity)

    @Delete
    suspend fun deleteTrapType(trapTypeEntity: TrapTypeEntity)

    @Query("SELECT * FROM TrapTypeEntity WHERE trapTypeId = :trapTypeId")
    fun getTrapType(trapTypeId: String): Flow<TrapTypeEntity>

    @Query("SELECT * FROM TrapTypeEntity")
    fun getTrapTypes(): Flow<List<TrapTypeEntity>>
}