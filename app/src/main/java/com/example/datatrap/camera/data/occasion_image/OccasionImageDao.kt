package com.example.datatrap.camera.data.occasion_image

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface OccasionImageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImage(occasionImageEntity: OccasionImageEntity)

    @Delete
    suspend fun deleteImage(occasionImageEntity: OccasionImageEntity)

    @Query("SELECT * FROM OccasionImageEntity WHERE occasionID = :occasionID")
    suspend fun getImageForOccasion(occasionID: String): OccasionImageEntity?
}