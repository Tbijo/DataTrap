package com.example.datatrap.camera.data.occasion_image

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey
import com.example.datatrap.occasion.data.occasion.OccasionEntity
import java.time.ZonedDateTime
import java.util.UUID

@Entity(foreignKeys = [
    ForeignKey(entity = OccasionEntity::class, parentColumns = ["occasionId"], childColumns = ["occasionID"], onDelete = CASCADE)
])
data class OccasionImageEntity(

    @PrimaryKey
    val occasionImgId: String = UUID.randomUUID().toString(),

    val imgName: String,

    val path: String,

    val note: String?,

    @ColumnInfo(index = true)
    val occasionID: String,

    val dateTimeCreated: ZonedDateTime,

    val dateTimeUpdated: ZonedDateTime?,
)