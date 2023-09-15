package com.example.datatrap.specie.presentation.specie_image

import com.example.datatrap.specie.data.specie_image.SpecieImageEntity

data class SpecieImageUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val specieImageEntity: SpecieImageEntity? = null,
)
