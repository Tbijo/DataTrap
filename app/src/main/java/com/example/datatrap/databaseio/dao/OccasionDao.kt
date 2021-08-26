package com.example.datatrap.databaseio.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.datatrap.models.Occasion

@Dao
interface OccasionDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOccasion(occasion: Occasion)

    @Update
    suspend fun updateOccasion(occasion: Occasion)

    @Delete
    suspend fun deleteOccasion(occasion: Occasion)

    @Query("SELECT * FROM occasion WHERE id = :idOccasion")
    suspend fun getOccasion(idOccasion: Long): LiveData<Occasion>

    @Query("SELECT * FROM occasion WHERE Session = :idSession")
    fun getOccasionsForSession(idSession: Long): LiveData<List<Occasion>>

    // zisti pocet akcii obsahujucich vybrane id sessiony
    @Query("SELECT COUNT(*) FROM occasion WHERE Session = :idSession")
    fun countOccasionsOfSession(idSession: Long): LiveData<Int>
}