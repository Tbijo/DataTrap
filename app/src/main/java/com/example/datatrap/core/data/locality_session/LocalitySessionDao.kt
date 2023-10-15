package com.example.datatrap.core.data.locality_session

import androidx.room.*

@Dao
interface LocalitySessionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocalitySessionCrossRef(localitySessionCrossRef: LocalitySessionCrossRef)

    @Delete
    suspend fun deleteLocalitySessionCrossRef(localitySessionCrossRef: LocalitySessionCrossRef)

    @Query("SELECT EXISTS (SELECT * FROM LocalitySessionCrossRef WHERE localityId = :localityId AND sessionId = :sessionId)")
    suspend fun existsLocalSessCrossRef(localityId: String, sessionId: String): Boolean
}