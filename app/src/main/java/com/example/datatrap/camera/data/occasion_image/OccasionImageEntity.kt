package com.example.datatrap.camera.data.occasion_image

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import com.example.datatrap.occasion.data.occasion.OccasionEntity
import java.util.UUID

@Entity(foreignKeys = [
    ForeignKey(entity = OccasionEntity::class, parentColumns = ["occasionId"], childColumns = ["occasionID"], onDelete = CASCADE)
])
data class OccasionImageEntity(

    @PrimaryKey
    var occasionImgId: String = UUID.randomUUID().toString(),

    var imgName: String,

    var path: String,

    var note: String?,

    @ColumnInfo(index = true)
    var occasionID: String,
)