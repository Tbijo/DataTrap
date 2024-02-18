package com.example.datatrap.settings.data.method

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.datatrap.settings.data.SettingsEntity
import java.time.ZonedDateTime
import java.util.UUID

@Entity(
    ignoredColumns = ["entityId", "entityName", "entityDateTimeCreated", "entityDateTimeUpdated"]
)
data class MethodEntity(

    @PrimaryKey
    var methodId: String = UUID.randomUUID().toString(),

    var methodName: String,

    var methodDateTimeCreated: ZonedDateTime,

    var methodDateTimeUpdated: ZonedDateTime?
): SettingsEntity(methodId, methodName, methodDateTimeCreated, methodDateTimeUpdated)
