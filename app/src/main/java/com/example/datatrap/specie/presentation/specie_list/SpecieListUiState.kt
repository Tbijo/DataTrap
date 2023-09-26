package com.example.datatrap.specie.presentation.specie_list

import com.example.datatrap.specie.data.SpecieEntity

data class SpecieListUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val specieList: List<SpecieEntity> = emptyList(),

    val searchTextFieldValue: String = "",
    val searchTextFieldHint: String = "Enter specie code...",
    val isSearchTextFieldHintVisible: Boolean = true,
)
