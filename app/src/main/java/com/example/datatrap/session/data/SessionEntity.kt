package com.example.datatrap.session.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey
import com.example.datatrap.project.data.ProjectEntity
import java.time.ZonedDateTime
import java.util.UUID

@Entity(foreignKeys = [
    ForeignKey(entity = ProjectEntity::class, parentColumns = ["projectId"], childColumns = ["projectID"], onDelete = CASCADE)
])
data class SessionEntity(

    @PrimaryKey
    val sessionId: String = UUID.randomUUID().toString(),

    val session: Int,

    @ColumnInfo(index = true)
    val projectID: String,

    val numOcc: Int,

    val sessionDateTimeCreated: ZonedDateTime = ZonedDateTime.now(),

    val sessionDateTimeUpdated: ZonedDateTime?,

    val sessionStart: ZonedDateTime = ZonedDateTime.now(),
)
