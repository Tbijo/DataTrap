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

    @Query("SELECT * FROM sm WHERE mouseId = :idMouse")
    fun getMouse(idMouse: Long): LiveData<Mouse>

    @Query("SELECT * FROM sm WHERE occasionID = :idOccasion")
    fun getMiceForOccasion(idOccasion: Long): LiveData<List<Mouse>>

    @Query("SELECT * FROM sm WHERE Code = :code ORDER BY catchDateTime DESC LIMIT 50")
    fun getMiceForRecapture(code: Int): LiveData<List<Mouse>>

    @Query("SELECT * FROM sm WHERE primeMouseID = :idMouse")
    fun getMiceForLog(idMouse: Long): LiveData<List<Mouse>>

    @Query("SELECT * FROM sm WHERE localityID = :localityId AND Code IS NOT NULL AND (:currentTime - catchDateTime) < :twoYears")
    fun getActiveMiceOfLocality(localityId: Long, currentTime: Long, twoYears: Long): LiveData<List<Mouse>>

    @Query("SELECT COUNT(Code) FROM sm WHERE localityID = :localityId AND Code IS NOT NULL AND Recapture = 0")
    fun countMiceForLocality(localityId: Long): LiveData<Int>
}