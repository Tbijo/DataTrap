package com.example.datatrap.specie.presentation.specie_add_edit

import com.example.datatrap.specie.data.SpecieEntity

data class SpecieUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val specieEntity: SpecieEntity? = null,
)
