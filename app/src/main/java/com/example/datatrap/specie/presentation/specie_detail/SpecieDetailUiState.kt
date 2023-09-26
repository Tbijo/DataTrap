package com.example.datatrap.specie.presentation.specie_detail

import com.example.datatrap.specie.data.SpecieEntity

data class SpecieDetailUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val specieEntity: SpecieEntity? = null,

    val imagePath: String? = null,
)
