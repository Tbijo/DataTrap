package com.example.datatrap.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

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

    var deviceID: String
)
