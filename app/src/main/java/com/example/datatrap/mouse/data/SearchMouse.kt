package com.example.datatrap.mouse.data

data class SearchMouse(
    var code: Int?,
    var sex: String?,
    var age: String?,
    var speciesID: Long?,
    var dateFrom: Long?,
    var dateTo: Long?,
    var gravidity: Boolean,
    var sexActive: Boolean,
    var lactating: Boolean
)
