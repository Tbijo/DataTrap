package com.example.datatrap.picture.data

import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import com.example.datatrap.occasion.data.Occasion

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
