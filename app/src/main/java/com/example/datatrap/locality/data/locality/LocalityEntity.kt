package com.example.datatrap.locality.data.locality

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.ZonedDateTime
import java.util.UUID

@Entity
data class LocalityEntity(

    @PrimaryKey
    val localityId: String = UUID.randomUUID().toString(),

    val localityName: String,

    val localityDateTimeCreated: ZonedDateTime = ZonedDateTime.now(),

    val localityDateTimeUpdated: ZonedDateTime?,

    val latitudeA: Float?,

    val longitudeA: Float?,

    val latitudeB: Float?,

    val longitudeB: Float?,

    val numSessions: Int,

    val note: String?
)
