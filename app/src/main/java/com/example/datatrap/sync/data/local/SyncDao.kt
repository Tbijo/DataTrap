package com.example.datatrap.sync.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.datatrap.mouse.data.MouseEntity
import com.example.datatrap.occasion.data.occasion.OccasionEntity
import com.example.datatrap.sync.data.remote.MouseImageSync
import com.example.datatrap.sync.data.remote.OccasionImageSync
import com.example.datatrap.sync.data.remote.SpecieImageSync

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

    @Query("SELECT imgName, path, note, specieID, uniqueCode FROM SpecieImageEntity WHERE uniqueCode >= :unixTime")
    suspend fun getSpecieImages(unixTime: Long): List<SpecieImageSync>
}