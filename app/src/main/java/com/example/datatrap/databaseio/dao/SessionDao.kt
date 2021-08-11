package com.example.datatrap.databaseio.dao

import androidx.room.*
import com.example.datatrap.models.Session
import kotlinx.coroutines.flow.Flow

@Dao
interface SessionDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSession(session: Session)

    @Update
    suspend fun updateSession(session: Session)

    @Delete
    suspend fun deleteSession(session: Session)

    @Query("SELECT * FROM sessions WHERE id = :idSession")
    suspend fun getSession(idSession: Long): Session

    @Query("SELECT * FROM sessions WHERE ProjectName = :projectName")
    fun getSessionsForProject(projectName: String): Flow<List<Session>>
}