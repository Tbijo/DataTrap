package com.example.datatrap.settings.data.protocol

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.datatrap.settings.data.SettingsEntity
import java.time.ZonedDateTime
import java.util.UUID

@Entity(
    ignoredColumns = ["entityId", "entityName", "entityDateTimeCreated", "entityDateTimeUpdated"]
)
data class ProtocolEntity(

    @PrimaryKey
    var protocolId: String = UUID.randomUUID().toString(),

    var protocolName: String,

    var protDateTimeCreated: ZonedDateTime,

    var protDateTimeUpdated: ZonedDateTime?
): SettingsEntity(protocolId, protocolName, protDateTimeCreated, protDateTimeUpdated)
