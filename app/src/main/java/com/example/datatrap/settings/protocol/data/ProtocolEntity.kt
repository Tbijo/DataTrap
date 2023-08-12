package com.example.datatrap.settings.protocol.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.ZonedDateTime
import java.util.UUID

@Entity
data class ProtocolEntity(

    @PrimaryKey
    var protocolId: String = UUID.randomUUID().toString(),

    var protocolName: String,

    var protDateTimeCreated: ZonedDateTime,

    var protDateTimeUpdated: ZonedDateTime?
)
