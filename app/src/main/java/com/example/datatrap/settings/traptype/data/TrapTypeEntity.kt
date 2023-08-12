package com.example.datatrap.settings.traptype.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.ZonedDateTime
import java.util.UUID

@Entity
data class TrapTypeEntity(

    @PrimaryKey
    var trapTypeId: String = UUID.randomUUID().toString(),

    var trapTypeName: String,

    var trapTypeDateTimeCreated: ZonedDateTime,

    var trapTypeDateTimeUpdated: ZonedDateTime?
)
