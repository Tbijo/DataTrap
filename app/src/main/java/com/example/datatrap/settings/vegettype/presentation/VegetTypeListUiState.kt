package com.example.datatrap.settings.vegettype.presentation

import com.example.datatrap.settings.vegettype.data.VegetType

data class VegetTypeListUiState(
    val vegetTypeList: List<VegetType>,
    val vegetTypeName: String? = null,
)