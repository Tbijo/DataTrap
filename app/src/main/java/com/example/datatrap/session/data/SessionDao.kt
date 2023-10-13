package com.example.datatrap.session.data

import androidx.room.*

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

    @Query("SELECT * FROM SessionEntity WHERE sessionId IN (:sessionIds)")
    suspend fun getSessionForSync(sessionIds: List<String>): List<SessionEntity>

    // SYNC
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSyncSession(sessionEntity: SessionEntity): Long

    // 604800 dlzka tyzdna v sekundach treba v milisekundach 604 800 000
//    @Query("SELECT * FROM SessionEntity WHERE projectID = :projectID GROUP BY sessionId HAVING MIN(ABS(sessionStart - :sessionStart)) <= 604800000")
//    suspend fun getNearSession(projectID: Long, sessionStart: Long): SessionEntity?

}