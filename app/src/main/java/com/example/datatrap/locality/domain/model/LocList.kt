package com.example.datatrap.locality.domain.model

import java.time.ZonedDateTime

data class LocList(

    var localityId: String,

    var localityName: String,

    var dateTime: ZonedDateTime,

    var xA: Float?,

    var yA: Float?,

    var numSessions: Int,
)
