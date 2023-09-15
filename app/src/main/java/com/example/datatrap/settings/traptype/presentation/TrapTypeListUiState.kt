package com.example.datatrap.settings.traptype.presentation

import com.example.datatrap.settings.traptype.data.TrapTypeEntity

data class TrapTypeListUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val trapTypeEntityList: List<TrapTypeEntity> = emptyList(),
    val trapTypeEntity: TrapTypeEntity? = null,
    val textNameValue: String = "",
    val textNameError: String? = null,
)