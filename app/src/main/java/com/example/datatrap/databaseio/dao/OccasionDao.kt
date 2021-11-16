package com.example.datatrap.databaseio.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.datatrap.models.Occasion
import com.example.datatrap.models.tuples.OccList

@Dao
interface OccasionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOccasion(occasion: Occasion)

    @Update
    suspend fun updateOccasion(occasion: Occasion)

    @Query("DELETE FROM Occasion WHERE occasionId = :occasionId")
    suspend fun deleteOccasion(occasionId: Long)

    @Query("SELECT * FROM Occasion WHERE occasionId = :occasionId")
    fun getOccasion(occasionId: Long): LiveData<Occasion>

    @Query("SELECT occasionId, localityID, occasion, occasionDateTimeCreated AS dateTime, numMice, numTraps, imgName FROM Occasion WHERE sessionID = :idSession")
    fun getOccasionsForSession(idSession: Long): LiveData<List<OccList>>

}