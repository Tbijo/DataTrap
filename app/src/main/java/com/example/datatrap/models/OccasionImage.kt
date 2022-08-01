package com.example.datatrap.models

import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import okhttp3.MultipartBody

@Entity(foreignKeys = [
    ForeignKey(entity = Occasion::class, parentColumns = ["occasionId"], childColumns = ["occasionID"], onDelete = CASCADE)
])
data class OccasionImage(

    @PrimaryKey(autoGenerate = true)
    var occasionImgId: Long,

    var imgName: String,

    var path: String,

    var note: String?,

    @ColumnInfo(index = true)
    var occasionID: Long,

    var uniqueCode: Long
)
