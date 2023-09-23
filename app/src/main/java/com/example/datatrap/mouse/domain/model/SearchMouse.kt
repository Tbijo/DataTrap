package com.example.datatrap.mouse.domain.model

import java.time.ZonedDateTime

data class SearchMouse(
    var code: Int?,
    var sex: String?,
    var age: String?,
    var speciesID: String,
    var dateFrom: ZonedDateTime?,
    var dateTo: ZonedDateTime?,
    var gravidity: Boolean,
    var sexActive: Boolean,
    var lactating: Boolean,
)
