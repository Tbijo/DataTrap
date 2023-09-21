package com.example.datatrap.session.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import com.example.datatrap.project.data.ProjectEntity
import java.time.ZonedDateTime
import java.util.UUID

@Entity(foreignKeys = [
    ForeignKey(entity = ProjectEntity::class, parentColumns = ["projectId"], childColumns = ["projectID"], onDelete = CASCADE)
])
data class SessionEntity(

    @PrimaryKey
    var sessionId: String = UUID.randomUUID().toString(),

    var session: Int,
    
    // foreign key
    @ColumnInfo(index = true)
    var projectID: String,

    var numOcc: Int,

    var sessionDateTimeCreated: ZonedDateTime = ZonedDateTime.now(),

    var sessionDateTimeUpdated: ZonedDateTime?,

    var sessionStart: ZonedDateTime = ZonedDateTime.now(),
)
