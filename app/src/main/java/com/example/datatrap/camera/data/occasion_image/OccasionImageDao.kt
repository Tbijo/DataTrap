package com.example.datatrap.camera.data.occasion_image

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.datatrap.sync.data.sync.OccasionImageSync

@Dao
interface OccasionImageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImage(occasionImage: OccasionImage)

    @Query("DELETE FROM OccasionImage WHERE occasionImgId = :occasionImgId")
    suspend fun deleteImage(occasionImgId: Long)

    @Query("SELECT * FROM OccasionImage WHERE occasionID = :occasionId")
    fun getImageForOccasion(occasionId: Long): LiveData<OccasionImage>

    @Query("SELECT imgName, path, note, occasionID, uniqueCode FROM OccasionImage WHERE uniqueCode >= :unixTime")
    suspend fun getOccasionImages(unixTime: Long): List<OccasionImageSync>
}