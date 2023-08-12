package com.example.datatrap.session.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface SessionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(sessionEntity: SessionEntity)

    @Update
    suspend fun updateSession(sessionEntity: SessionEntity)

    @Delete
    suspend fun deleteSession(sessionEntity: SessionEntity)

    @Query("SELECT * FROM SessionEntity WHERE projectID = :projectId")
    fun getSessionsForProject(projectId: Long): LiveData<List<SessionEntity>>

    @Query("SELECT * FROM session WHERE sessionId IN (:sessionIds)")
    suspend fun getSessionForSync(sessionIds: List<Long>): List<SessionEntity>

    // SYNC
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSyncSession(sessionEntity: SessionEntity): Long

    @Query("SELECT * FROM session WHERE projectID = :projectID AND sessionStart = :sessionStart")
    suspend fun getSession(projectID: Long, sessionStart: Long): SessionEntity?

    // 604800 dlzka tyzdna v sekundach treba v milisekundach 604 800 000
    @Query("SELECT * FROM session WHERE projectID = :projectID GROUP BY sessionId HAVING MIN(ABS(sessionStart - :sessionStart)) <= 604800000")
    suspend fun getNearSession(projectID: Long, sessionStart: Long): SessionEntity?

}