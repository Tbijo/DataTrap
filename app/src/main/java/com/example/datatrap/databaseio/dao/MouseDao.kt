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

    // zobrazenie predchadzajucich udajov o mysi
    // na zmenenie pohlavia ak sa pri prvom chyteni zle zadalo a pri druhom cyteni je uz starsi tak to lepsie vidiet
    @Query("SELECT * FROM sm WHERE Code = :code ORDER BY localityID")
    fun getMiceForCode(code: Int): LiveData<List<Mouse>>

    @Query("SELECT * FROM sm WHERE localityID = :localityId ORDER BY catchDateTime DESC")
    fun getOldMiceForLocality(localityId: Long): LiveData<List<Mouse>>

    // pre recapture
    @Query("SELECT * FROM sm WHERE Code = :code")
    fun searchMice(code: Int): LiveData<List<Mouse>>

    @Query("SELECT COUNT(*) FROM sm WHERE localityID = :localityId")
    fun countMiceForLocality(localityId: Long): LiveData<Int>
}