package com.example.datatrap.mouse.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MouseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMouse(mouseEntity: MouseEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMice(mice: List<MouseEntity>)

    @Delete
    suspend fun deleteMouse(mouseEntity: MouseEntity)

    @Query("SELECT * FROM MouseEntity WHERE mouseId = :mouseId")
    suspend fun getMouse(mouseId: String): MouseEntity

    @Query("SELECT * FROM MouseEntity")
    suspend fun getMice(): List<MouseEntity>

    @Query("SELECT * FROM MouseEntity WHERE occasionID = :occasionID")
    fun getMiceForOccasion(occasionID: String): Flow<List<MouseEntity>>

    @Query("SELECT * FROM MouseEntity WHERE primeMouseID = :primeMouseID")
    suspend fun getMiceByPrimeMouseID(primeMouseID: String): List<MouseEntity>

}