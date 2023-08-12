package com.example.datatrap.settings.methodtype.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.ZonedDateTime
import java.util.UUID

@Entity
data class MethodTypeEntity(

    @PrimaryKey
    var methodTypeId: String = UUID.randomUUID().toString(),

    var methodTypeName: String,

    var methTypeDateTimeCreated: ZonedDateTime,

    var methTypeDateTimeUpdated: ZonedDateTime?
)
