package com.example.datatrap.settings.vegettype.presentation

import com.example.datatrap.settings.vegettype.data.VegetTypeEntity

data class VegetTypeListUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val vegetTypeEntityList: List<VegetTypeEntity> = emptyList(),
    val vegetTypeEntity: VegetTypeEntity? = null,
    val textNameValue: String = "",
    val textNameError: String? = null,
)