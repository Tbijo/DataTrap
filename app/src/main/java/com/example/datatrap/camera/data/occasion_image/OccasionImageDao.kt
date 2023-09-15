package com.example.datatrap.camera.data.occasion_image

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.datatrap.sync.data.OccasionImageSync
import kotlinx.coroutines.flow.Flow

@Dao
interface OccasionImageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImage(occasionImageEntity: OccasionImageEntity)

    @Delete
    suspend fun deleteImage(occasionImageEntity: OccasionImageEntity)

    @Query("SELECT * FROM OccasionImageEntity WHERE occasionID = :occasionId")
    fun getImageForOccasion(occasionId: String): Flow<OccasionImageEntity?>

    @Query("SELECT imgName, path, note, occasionID, uniqueCode FROM OccasionImageEntity WHERE uniqueCode >= :unixTime")
    suspend fun getOccasionImages(unixTime: Long): List<OccasionImageSync>
}