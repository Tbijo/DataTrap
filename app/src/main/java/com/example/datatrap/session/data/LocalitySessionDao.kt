package com.example.datatrap.session.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface LocalitySessionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocalitySessionCrossRef(localitySessionCrossRef: LocalitySessionCrossRef)

    @Delete
    suspend fun deleteLocalitySessionCrossRef(localitySessionCrossRef: LocalitySessionCrossRef)

    @Query("SELECT EXISTS (SELECT * FROM LocalitySessionCrossRef WHERE localityId = :localityId AND sessionId = :sessionId)")
    fun existsLocalSessCrossRef(localityId: Long, sessionId: Long): LiveData<Boolean>
}