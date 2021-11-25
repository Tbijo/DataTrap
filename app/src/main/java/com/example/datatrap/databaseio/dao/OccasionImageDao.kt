package com.example.datatrap.databaseio.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.datatrap.models.OccasionImage

@Dao
interface OccasionImageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImage(occasionImage: OccasionImage)

    @Query("DELETE FROM OccasionImage WHERE occasionImgId = :occasionImgId")
    suspend fun deleteImage(occasionImgId: Long)

    @Query("SELECT * FROM OccasionImage WHERE occasionID = :occasionId")
    fun getImageForOccasion(occasionId: Long): LiveData<OccasionImage>
}