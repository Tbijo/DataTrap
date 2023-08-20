package com.example.datatrap.settings.vegettype.presentation

import com.example.datatrap.settings.vegettype.data.VegetTypeEntity

data class VegetTypeListUiState(
    val vegetTypeEntityList: List<VegetTypeEntity> = emptyList(),
    val vegetTypeEntity: VegetTypeEntity? = null,
    val isLoading: Boolean = true,
    val textNameValue: String = "",
    val textNameError: String? = null,
)