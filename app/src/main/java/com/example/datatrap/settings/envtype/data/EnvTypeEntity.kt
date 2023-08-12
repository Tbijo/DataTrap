package com.example.datatrap.settings.envtype.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.ZonedDateTime
import java.util.UUID

@Entity
data class EnvTypeEntity(

    @PrimaryKey
    var envTypeId: String = UUID.randomUUID().toString(),

    var envTypeName: String,

    var envTypeDateTimeCreated: ZonedDateTime,

    var envTypeDateTimeUpdated: ZonedDateTime?
)
