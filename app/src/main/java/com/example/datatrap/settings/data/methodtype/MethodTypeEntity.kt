package com.example.datatrap.settings.data.methodtype

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.datatrap.settings.data.SettingsEntity
import java.time.ZonedDateTime
import java.util.UUID

@Entity(
    ignoredColumns = ["entityId", "entityName", "entityDateTimeCreated", "entityDateTimeUpdated"]
)
data class MethodTypeEntity(

    @PrimaryKey
    var methodTypeId: String = UUID.randomUUID().toString(),

    var methodTypeName: String,

    var methTypeDateTimeCreated: ZonedDateTime,

    var methTypeDateTimeUpdated: ZonedDateTime?
): SettingsEntity(methodTypeId, methodTypeName, methTypeDateTimeCreated, methTypeDateTimeUpdated)
