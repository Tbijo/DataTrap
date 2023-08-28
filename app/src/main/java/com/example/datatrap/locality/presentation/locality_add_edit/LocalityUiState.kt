package com.example.datatrap.locality.presentation.locality_add_edit

import com.example.datatrap.locality.data.LocalityEntity

data class LocalityUiState(
    val localityList: LocalityEntity? = null,
    val isLoading: Boolean = true,
    val error: String = "",

    val localityName: String = "",
    val localityNameError: String? = null,
)
