package com.example.datatrap.occasion.data.occasion

import androidx.room.*

@Dao
interface OccasionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOccasion(occasionEntity: OccasionEntity)

    @Delete
    suspend fun deleteOccasion(occasionEntity: OccasionEntity)

    @Query("SELECT * FROM OccasionEntity WHERE occasionId = :occasionId")
    suspend fun getOccasion(occasionId: String): OccasionEntity

    @Query("SELECT * FROM OccasionEntity WHERE sessionID = :sessionID")
    fun getOccasionsForSession(sessionID: String): List<OccasionEntity>
}