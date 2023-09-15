package com.example.datatrap.occasion.data.occasion

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface OccasionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOccasion(occasionEntity: OccasionEntity): String

    @Delete
    suspend fun deleteOccasion(occasionEntity: OccasionEntity)

    @Query("SELECT * FROM OccasionEntity WHERE occasionId = :occasionId")
    fun getOccasion(occasionId: String): Flow<OccasionEntity>

    @Query("SELECT * FROM OccasionEntity WHERE sessionID = :sessionID")
    fun getOccasionsForSession(sessionID: String): Flow<List<OccasionEntity>>

    // SYNC
    @Query("SELECT * FROM OccasionEntity WHERE occasionId IN (:occasionIds)")
    suspend fun getOccasionForSync(occasionIds: List<String>): List<OccasionEntity>

    @Query("SELECT * FROM OccasionEntity WHERE sessionID = :sessionID AND occasionStart = :occasionStart")
    suspend fun getSyncOccasion(sessionID: String, occasionStart: Long): OccasionEntity?

}