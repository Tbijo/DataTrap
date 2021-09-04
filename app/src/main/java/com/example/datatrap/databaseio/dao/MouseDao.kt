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

    @Query("SELECT * FROM sm WHERE occasionID = :idOccasion")
    fun getMiceForOccasion(idOccasion: Long): LiveData<List<Mouse>>

    @Query("SELECT * FROM sm WHERE Code = :code ORDER BY localityID")
    fun getMiceForCode(code: Int): LiveData<List<Mouse>>

    // pre recapture
    @Query("SELECT * FROM sm WHERE Code = :code LIMIT 20")
    fun searchMice(code: Int): LiveData<List<Mouse>>

    @Query("SELECT COUNT(*) FROM sm WHERE localityID = :localityId")
    fun countMiceForLocality(localityId: Long): LiveData<Int>
}