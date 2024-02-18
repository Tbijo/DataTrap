package com.example.datatrap.settings.data.traptype

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.datatrap.settings.data.SettingsEntity
import java.time.ZonedDateTime
import java.util.UUID

@Entity(
    ignoredColumns = ["entityId", "entityName", "entityDateTimeCreated", "entityDateTimeUpdated"]
)
data class TrapTypeEntity(

    @PrimaryKey
    var trapTypeId: String = UUID.randomUUID().toString(),

    var trapTypeName: String,

    var trapTypeDateTimeCreated: ZonedDateTime,

    var trapTypeDateTimeUpdated: ZonedDateTime?
): SettingsEntity(trapTypeId, trapTypeName, trapTypeDateTimeCreated, trapTypeDateTimeUpdated)
