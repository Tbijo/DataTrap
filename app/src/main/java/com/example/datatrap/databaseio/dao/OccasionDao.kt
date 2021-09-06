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

    @Query("SELECT * FROM occasions WHERE occasionId = :occasionId")
    fun getOccasion(occasionId: Long): LiveData<Occasion>

    @Query("SELECT * FROM occasions WHERE sessionID = :idSession")
    fun getOccasionsForSession(idSession: Long): LiveData<List<Occasion>>

}