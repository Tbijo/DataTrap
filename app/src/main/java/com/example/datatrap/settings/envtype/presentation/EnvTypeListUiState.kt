package com.example.datatrap.settings.envtype.presentation

import com.example.datatrap.settings.envtype.data.EnvTypeEntity

data class EnvTypeListUiState(
    val envTypeEntityList: List<EnvTypeEntity> = emptyList(),
    val envTypeEntity: EnvTypeEntity? = null,
    val isLoading: Boolean = true,
    val textNameValue: String = "",
    val textNameError: String? = null,
)