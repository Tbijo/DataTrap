package com.example.datatrap.locality.presentation.locality_add_edit

data class LocalityUiState(
    val isLoading: Boolean = true,
    val error: String? = null,

    val localityName: String = "",
    val localityNameError: String? = null,

    val numSessions: String = "",
    val note: String = "",

    val latitudeA: String = "",
    val longitudeA: String = "",
    val latitudeB: String = "",
    val longitudeB: String = "",
)
