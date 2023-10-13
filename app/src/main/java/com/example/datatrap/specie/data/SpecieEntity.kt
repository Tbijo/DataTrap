package com.example.datatrap.specie.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.ZonedDateTime
import java.util.UUID

@Entity
data class SpecieEntity(
    @PrimaryKey
    var specieId: String = UUID.randomUUID().toString(),

    var speciesCode: String,

    var fullName: String,

    var synonym: String?,

    var authority: String?,

    var description: String?,

    var isSmallMammal: Boolean,

    var upperFingers: Int?,

    var minWeight: Float?, //kontrloly pri vstupoch

    var maxWeight: Float?, //kontrloly pri vstupoch

    var bodyLength: Float?,

    var tailLength: Float?,

    var feetLengthMin: Float?,

    var feetLengthMax: Float?,

    var note: String?,

    var specieDateTimeCreated: ZonedDateTime,

    var specieDateTimeUpdated: ZonedDateTime?
)