package com.example.datatrap.specie.data

data class SpecSelectList(
    var specieId: Long,

    var speciesCode: String,

    var upperFingers: Int?,

    var minWeight: Float?,

    var maxWeight: Float?
)
