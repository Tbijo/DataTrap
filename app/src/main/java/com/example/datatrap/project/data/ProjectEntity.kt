package com.example.datatrap.project.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.ZonedDateTime
import java.util.UUID

@Entity
data class ProjectEntity(

    @PrimaryKey
    var projectId: String = UUID.randomUUID().toString(),

    var projectName: String,

    var projectDateTimeCreated: ZonedDateTime,

    var projectDateTimeUpdated: ZonedDateTime?,

    var numLocal: Int,

    var numMice: Int,

    var projectStart: Long
)
