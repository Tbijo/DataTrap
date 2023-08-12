package com.example.datatrap.settings.vegettype.presentation

import com.example.datatrap.settings.vegettype.data.VegetTypeEntity

data class VegetTypeListUiState(
    val vegetTypeEntityList: List<VegetTypeEntity>,
    val vegetTypeName: String? = null,
)