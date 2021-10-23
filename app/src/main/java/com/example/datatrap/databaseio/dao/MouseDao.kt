package com.example.datatrap.databaseio.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.datatrap.models.Mouse

@Dao
interface MouseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMouse(mouse: Mouse)

    @Update
    suspend fun updateMouse(mouse: Mouse)

    @Delete
    suspend fun deleteMouse(mouse: Mouse)

    @Query("SELECT * FROM Mouse WHERE mouseId = :idMouse")
    suspend fun getMouse(idMouse: Long): Mouse?

    @Query("SELECT * FROM Mouse WHERE occasionID = :idOccasion")
    fun getMiceForOccasion(idOccasion: Long): LiveData<List<Mouse>>

    @Query("SELECT * FROM Mouse WHERE Code = :code ORDER BY mouseDateTimeCreated DESC LIMIT 100")
    fun getMiceForRecapture(code: Int): LiveData<List<Mouse>>

    @Query("SELECT * FROM Mouse WHERE primeMouseID = :idMouse")
    fun getMiceForLog(idMouse: Long): LiveData<List<Mouse>>

    @Query("SELECT * FROM Mouse WHERE localityID = :localityId AND Code IS NOT NULL AND (:currentTime - mouseDateTimeCreated) < :twoYears")
    fun getActiveMiceOfLocality(localityId: Long, currentTime: Long, twoYears: Long): LiveData<List<Mouse>>

    @Query("SELECT COUNT(Code) FROM Mouse WHERE localityID = :localityId AND Code IS NOT NULL AND Recapture = 0")
    suspend fun countMiceForLocality(localityId: Long): Int
}