package com.example.datatrap.locality.presentation.locality_map

import com.example.datatrap.locality.data.locality.LocalityEntity

data class LocalityMapUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val localityList: List<LocalityEntity> = emptyList(),
)
