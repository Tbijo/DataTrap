package com.example.datatrap.sync.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.datatrap.locality.data.locality.LocalityEntity
import com.example.datatrap.mouse.data.MouseEntity
import com.example.datatrap.occasion.data.occasion.OccasionEntity
import com.example.datatrap.project.data.ProjectEntity
import com.example.datatrap.session.data.SessionEntity
import com.example.datatrap.sync.data.remote.MouseImageSync
import com.example.datatrap.sync.data.remote.OccasionImageSync
import com.example.datatrap.sync.data.remote.SpecieImageSync
import java.time.ZonedDateTime

@Dao
interface SyncDao {
    // SYNC
    // zoznam novych alebo upravenych mysi podla posledneho datumu sync
    @Query("SELECT * FROM MouseEntity WHERE mouseDateTimeCreated >= :lastSync OR mouseDateTimeUpdated >= :lastSync")
    suspend fun getMiceForSync(lastSync: Long): List<MouseEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSynctMouse(mouseEntity: MouseEntity)

    @Query("SELECT * FROM MouseEntity WHERE occasionID = :occasionID AND mouseCaught = :mouseCaught")
    suspend fun getSyncMouse(occasionID: String, mouseCaught: Long): MouseEntity?

    // sync occasion
    // SYNC
    @Query("SELECT * FROM OccasionEntity WHERE occasionId IN (:occasionIds)")
    suspend fun getOccasionForSync(occasionIds: List<String>): List<OccasionEntity>

    @Query("SELECT * FROM OccasionEntity WHERE sessionID = :sessionID AND occasionStart = :occasionStart")
    suspend fun getSyncOccasion(sessionID: String, occasionStart: Long): OccasionEntity?

    // Image
    @Query("SELECT imgName, path, note, occasionID FROM OccasionImageEntity")
    suspend fun getOccasionImages(unixTime: Long): List<OccasionImageSync>

    @Query("SELECT imgName, path, note FROM MouseImageEntity")
    suspend fun getMouseImages(unixTime: Long): List<MouseImageSync>

    @Query("SELECT imageUri, note, specieID FROM SpecieImageEntity WHERE specieImgId >= :unixTime")
    suspend fun getSpecieImages(unixTime: Long): List<SpecieImageSync>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSyncLocality(localityEntity: LocalityEntity)

    // Sync
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSyncProject(projectEntity: ProjectEntity)

    @Query("SELECT * FROM ProjectEntity WHERE projectId IN (:projectIds)")
    suspend fun getProjectForSync(projectIds: List<Long>): List<ProjectEntity>

    // SYNC
    @Query("SELECT * FROM LocalityEntity WHERE localityId IN (:localityIds)")
    suspend fun getLocalityForSync(localityIds: List<String>): List<LocalityEntity>

    // SYNC
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSyncSession(sessionEntity: SessionEntity): Long

    @Query("SELECT * FROM SessionEntity WHERE sessionId IN (:sessionIds)")
    suspend fun getSessionForSync(sessionIds: List<String>): List<SessionEntity>

    // 604800 dlzka tyzdna v sekundach treba v milisekundach 604 800 000
    @Query("SELECT * FROM SessionEntity WHERE projectID = :projectID GROUP BY sessionId HAVING MIN(ABS(sessionStart - :sessionStart)) <= 604800000")
    suspend fun getNearSession(projectID: String, sessionStart: ZonedDateTime): SessionEntity?
}