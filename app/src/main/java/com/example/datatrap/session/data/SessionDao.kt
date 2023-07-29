package com.example.datatrap.session.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.datatrap.session.data.Session

@Dao
interface SessionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: Session)

    @Update
    suspend fun updateSession(session: Session)

    @Delete
    suspend fun deleteSession(session: Session)

    @Query("SELECT * FROM Session WHERE projectID = :projectId")
    fun getSessionsForProject(projectId: Long): LiveData<List<Session>>

    @Query("SELECT * FROM session WHERE sessionId IN (:sessionIds)")
    suspend fun getSessionForSync(sessionIds: List<Long>): List<Session>

    // SYNC
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSyncSession(session: Session): Long

    @Query("SELECT * FROM session WHERE projectID = :projectID AND sessionStart = :sessionStart")
    suspend fun getSession(projectID: Long, sessionStart: Long): Session?

    // 604800 dlzka tyzdna v sekundach treba v milisekundach 604 800 000
    @Query("SELECT * FROM session WHERE projectID = :projectID GROUP BY sessionId HAVING MIN(ABS(sessionStart - :sessionStart)) <= 604800000")
    suspend fun getNearSession(projectID: Long, sessionStart: Long): Session?

}