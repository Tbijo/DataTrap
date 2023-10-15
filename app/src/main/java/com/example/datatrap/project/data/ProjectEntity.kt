package com.example.datatrap.project.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.ZonedDateTime
import java.util.UUID

@Entity
data class ProjectEntity(

    @PrimaryKey
    val projectId: String = UUID.randomUUID().toString(),

    val projectName: String,

    val numLocal: Int,

    val numMice: Int,

    val projectDateTimeCreated: ZonedDateTime = ZonedDateTime.now(),

    val projectDateTimeUpdated: ZonedDateTime?,

    val projectStart: ZonedDateTime = ZonedDateTime.now(),
)
