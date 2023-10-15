package com.example.datatrap.specie.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.ZonedDateTime
import java.util.UUID

@Entity
data class SpecieEntity(
    @PrimaryKey
    val specieId: String = UUID.randomUUID().toString(),

    val speciesCode: String,

    val fullName: String,

    val synonym: String?,

    val authority: String?,

    val description: String?,

    val isSmallMammal: Boolean,

    val upperFingers: Int?,

    val minWeight: Float?, //kontrloly pri vstupoch

    val maxWeight: Float?, //kontrloly pri vstupoch

    val bodyLength: Float?,

    val tailLength: Float?,

    val feetLengthMin: Float?,

    val feetLengthMax: Float?,

    val note: String?,

    val specieDateTimeCreated: ZonedDateTime,

    val specieDateTimeUpdated: ZonedDateTime?
)