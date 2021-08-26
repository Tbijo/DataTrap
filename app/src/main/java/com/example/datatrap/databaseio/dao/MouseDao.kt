package com.example.datatrap.databaseio.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.datatrap.models.Mouse

@Dao
interface MouseDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMouse(mouse: Mouse)

    @Update
    suspend fun updateMouse(mouse: Mouse)

    @Delete
    suspend fun deleteMouse(mouse: Mouse)

    @Query("SELECT * FROM sm WHERE id = :idMouse")
    suspend fun getMouse(idMouse: Long): LiveData<Mouse>

    @Query("SELECT * FROM sm WHERE Occasion_ID = :idOccasion")
    fun getMiceForOccasion(idOccasion: Int): LiveData<List<Mouse>>

    // mozno pridat metodu co vrati najnovsieho potkana
}