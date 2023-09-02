package com.example.datatrap.locality.domain

sealed class LocationException(message: String): Exception(message)
class MissingPermissionException: LocationException("Missing location permission")
class GPSException: LocationException("GPS is disabled")