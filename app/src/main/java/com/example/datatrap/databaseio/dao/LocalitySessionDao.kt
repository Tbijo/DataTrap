package com.example.datatrap.databaseio.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.datatrap.models.localitysession.LocalitySessionCrossRef

@Dao
interface LocalitySessionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocalitySessionCrossRef(localitySessionCrossRef: LocalitySessionCrossRef)

    @Delete
    suspend fun deleteLocalitySessionCrossRef(localitySessionCrossRef: LocalitySessionCrossRef)

    @Query("SELECT EXISTS (SELECT * FROM LocalitySessionCrossRef WHERE localityId = :localityId AND sessionId = :sessionId)")
    fun existsLocalSessCrossRef(localityId: Long, sessionId: Long): LiveData<Boolean>
}