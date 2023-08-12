package com.example.datatrap.settings.envtype.presentation

import com.example.datatrap.settings.envtype.data.EnvTypeEntity

data class EnvTypeListUiState(
    val envTypeEntityList: List<EnvTypeEntity>,
    val envTypeName: String? = null,
)