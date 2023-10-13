package com.example.datatrap.mouse.data

import androidx.room.*

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
    suspend fun getMiceForOccasion(occasionID: String): List<MouseEntity>

    @Query("SELECT * FROM MouseEntity WHERE primeMouseID = :primeMouseID")
    suspend fun getMiceByPrimeMouseID(primeMouseID: String): List<MouseEntity>

}