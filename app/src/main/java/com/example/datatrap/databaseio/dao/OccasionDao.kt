package com.example.datatrap.databaseio.dao

import androidx.room.*
import com.example.datatrap.models.Occasion
import kotlinx.coroutines.flow.Flow

@Dao
interface OccasionDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOccasion(occasion: Occasion)

    @Update
    suspend fun updateOccasion(occasion: Occasion)

    @Delete
    suspend fun deleteOccasion(occasion: Occasion)

    @Query("SELECT * FROM occasion WHERE id = :idOccasion")
    suspend fun getOccasion(idOccasion: Long): Occasion

    @Query("SELECT * FROM occasion WHERE Session = :idSession")
    fun getOccasionsForSession(idSession: Long): Flow<List<Occasion>>

    // zisti pocet akcii obsahujucich vybrane id sessiony
    @Query("SELECT COUNT(*) FROM occasion WHERE Session = :idSession")
    fun countOccasionsOfSession(idSession: Long): Flow<Int>
}