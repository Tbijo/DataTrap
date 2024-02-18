package com.example.datatrap.session.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SessionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(sessionEntity: SessionEntity)

    @Delete
    suspend fun deleteSession(sessionEntity: SessionEntity)

    @Query("SELECT * FROM SessionEntity WHERE sessionId = :sessionId")
    suspend fun getSession(sessionId: String): SessionEntity

    @Query("SELECT * FROM SessionEntity WHERE projectID = :projectId")
    suspend fun getSessionsForProject(projectId: String): List<SessionEntity>

}