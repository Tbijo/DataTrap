package com.example.datatrap.settings.data.env_type

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.datatrap.settings.data.SettingsEntity
import java.time.ZonedDateTime
import java.util.UUID

@Entity(
    ignoredColumns = ["entityId", "entityName", "entityDateTimeCreated", "entityDateTimeUpdated"]
)
data class EnvTypeEntity(

    @PrimaryKey
    var envTypeId: String = UUID.randomUUID().toString(),

    var envTypeName: String,

    var envTypeDateTimeCreated: ZonedDateTime,

    var envTypeDateTimeUpdated: ZonedDateTime?
): SettingsEntity(envTypeId, envTypeName, envTypeDateTimeCreated, envTypeDateTimeUpdated)
