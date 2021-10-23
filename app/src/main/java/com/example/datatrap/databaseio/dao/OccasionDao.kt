package com.example.datatrap.databaseio.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.datatrap.models.Occasion

@Dao
interface OccasionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOccasion(occasion: Occasion)

    @Update
    suspend fun updateOccasion(occasion: Occasion)

    @Delete
    suspend fun deleteOccasion(occasion: Occasion)

    @Query("SELECT * FROM Occasion WHERE occasionId = :occasionId")
    suspend fun getOccasion(occasionId: Long): Occasion?

    @Query("SELECT * FROM Occasion WHERE sessionID = :idSession")
    fun getOccasionsForSession(idSession: Long): LiveData<List<Occasion>>

}