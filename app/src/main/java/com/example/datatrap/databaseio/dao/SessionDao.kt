package com.example.datatrap.databaseio.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.datatrap.models.Session

@Dao
interface SessionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: Session)

    @Update
    suspend fun updateSession(session: Session)

    @Delete
    suspend fun deleteSession(session: Session)

    @Query("SELECT * FROM Session WHERE sessionId = :sessionId")
    suspend fun getSession(sessionId: Long): Session

    @Query("SELECT * FROM Session WHERE projectID = :projectId")
    fun getSessionsForProject(projectId: Long): LiveData<List<Session>>
}