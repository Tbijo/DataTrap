package com.example.datatrap.locality.domain

import android.location.Location
import com.example.datatrap.sync.utils.Result
import kotlinx.coroutines.flow.Flow

interface LocationClient {

    // interval - how often we want an update
    // Flow will emit everytime there is a new location that we fetch
    fun getLocation(): Flow<Result<Location, LocationException>>

    fun cancelLocationProvider()
}