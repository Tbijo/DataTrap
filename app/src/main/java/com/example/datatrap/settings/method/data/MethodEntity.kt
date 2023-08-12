package com.example.datatrap.settings.method.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.ZonedDateTime
import java.util.UUID

@Entity
data class MethodEntity(

    @PrimaryKey
    var methodId: String = UUID.randomUUID().toString(),

    var methodName: String,

    var methodDateTimeCreated: ZonedDateTime,

    var methodDateTimeUpdated: ZonedDateTime?
)
