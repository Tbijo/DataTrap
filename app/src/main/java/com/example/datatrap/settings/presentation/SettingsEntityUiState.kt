package com.example.datatrap.settings.presentation

import com.example.datatrap.settings.data.SettingsEntity

data class SettingsEntityUiState(
    val title: String = "EnvType List",
    val iconDescription: String = "Add EnvType",
    val placeholder: String = "Environment Type",
    val label: String = "Environment Type",

    val isLoading: Boolean = true,
    val error: String? = null,

    val entityList: List<SettingsEntity> = emptyList(),
    val entity: SettingsEntity? = null,

    val textNameValue: String = "",
    val textNameError: String? = null,
)
