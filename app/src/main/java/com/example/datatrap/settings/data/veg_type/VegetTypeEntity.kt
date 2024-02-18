package com.example.datatrap.settings.data.veg_type

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.datatrap.settings.data.SettingsEntity
import java.time.ZonedDateTime
import java.util.UUID

@Entity(
    ignoredColumns = ["entityId", "entityName", "entityDateTimeCreated", "entityDateTimeUpdated"]
)
data class VegetTypeEntity(

    @PrimaryKey
    var vegetTypeId: String = UUID.randomUUID().toString(),

    var vegetTypeName: String,

    var vegTypeDateTimeCreated: ZonedDateTime,

    var vegTypeDateTimeUpdated: ZonedDateTime?
): SettingsEntity(vegetTypeId, vegetTypeName, vegTypeDateTimeCreated, vegTypeDateTimeUpdated)
