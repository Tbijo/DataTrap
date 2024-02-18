package com.example.datatrap.settings.data

import com.example.datatrap.settings.data.env_type.EnvTypeEntity
import com.example.datatrap.settings.data.method.MethodEntity
import com.example.datatrap.settings.data.methodtype.MethodTypeEntity
import com.example.datatrap.settings.data.protocol.ProtocolEntity
import com.example.datatrap.settings.data.traptype.TrapTypeEntity
import com.example.datatrap.settings.data.veg_type.VegetTypeEntity
import java.time.ZonedDateTime
import java.util.UUID

open class SettingsEntity(
    var entityId: String = UUID.randomUUID().toString(),

    var entityName: String,

    var entityDateTimeCreated: ZonedDateTime,

    var entityDateTimeUpdated: ZonedDateTime?,
) {
    fun toEnvTypeEntity() = EnvTypeEntity(
        envTypeName = entityName,
        envTypeDateTimeCreated = entityDateTimeCreated,
        envTypeDateTimeUpdated = entityDateTimeUpdated,
    )

    fun toMethodEntity() = MethodEntity(
        methodName = entityName,
        methodDateTimeCreated = entityDateTimeCreated,
        methodDateTimeUpdated = entityDateTimeUpdated,
    )

    fun toMethodTypeEntity() = MethodTypeEntity(
        methodTypeName = entityName,
        methTypeDateTimeCreated = entityDateTimeCreated,
        methTypeDateTimeUpdated = entityDateTimeUpdated,
    )

    fun toProtocolEntity() = ProtocolEntity(
        protocolName = entityName,
        protDateTimeCreated = entityDateTimeCreated,
        protDateTimeUpdated = entityDateTimeUpdated,
    )

    fun toTrapTypeEntity() = TrapTypeEntity(
        trapTypeName = entityName,
        trapTypeDateTimeCreated = entityDateTimeCreated,
        trapTypeDateTimeUpdated = entityDateTimeUpdated,
    )

    fun toVegetTypeEntity() = VegetTypeEntity(
        vegetTypeName = entityName,
        vegTypeDateTimeCreated = entityDateTimeCreated,
        vegTypeDateTimeUpdated = entityDateTimeUpdated,
    )
}