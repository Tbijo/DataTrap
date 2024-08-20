package com.example.datatrap.locality.domain

import com.example.datatrap.sync.utils.Error

sealed class LocationException(message: String): Exception(message), Error
class MissingPermissionException: LocationException("Missing location permission")
class GPSException: LocationException("GPS is disabled")