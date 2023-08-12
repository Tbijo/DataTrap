package com.example.datatrap.settings.vegettype.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.ZonedDateTime
import java.util.UUID

@Entity
data class VegetTypeEntity(

    @PrimaryKey
    var vegetTypeId: String = UUID.randomUUID().toString(),

    var vegetTypeName: String,

    var vegTypeDateTimeCreated: ZonedDateTime,

    var vegTypeDateTimeUpdated: ZonedDateTime?
)
