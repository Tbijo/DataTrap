package com.example.datatrap.locality.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.ZonedDateTime
import java.util.UUID

@Entity
data class LocalityEntity(

    @PrimaryKey
    var localityId: String = UUID.randomUUID().toString(),

    var localityName: String,

    var localityDateTimeCreated: ZonedDateTime,

    var localityDateTimeUpdated: ZonedDateTime?,

    var xA: Float?, // latitudeA

    var yA: Float?, // longitudeA

    var xB: Float?, // latitudeB

    var yB: Float?, // longitudeB

    var numSessions: Int,

    var note: String?
)
