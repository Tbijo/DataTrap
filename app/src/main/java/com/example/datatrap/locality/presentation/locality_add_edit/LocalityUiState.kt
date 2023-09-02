package com.example.datatrap.locality.presentation.locality_add_edit

import com.example.datatrap.locality.data.locality.LocalityEntity

data class LocalityUiState(
    val localityEntity: LocalityEntity? = null,
    val isLoading: Boolean = true,
    val error: String = "",

    val localityName: String = "",
    val localityNameError: String? = null,

    val numSessions: String = "",
    val note: String = "",

    val latitudeA: String = "",
    val longitudeA: String = "",
    val latitudeB: String = "",
    val longitudeB: String = "",
)
