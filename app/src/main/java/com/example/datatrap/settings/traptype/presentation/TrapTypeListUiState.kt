package com.example.datatrap.settings.traptype.presentation

import com.example.datatrap.settings.traptype.data.TrapTypeEntity

data class TrapTypeListUiState(
    val trapTypeEntityList: List<TrapTypeEntity> = emptyList(),
    val trapTypeEntity: TrapTypeEntity? = null,
    val isLoading: Boolean = true,
    val textNameValue: String = "",
    val textNameError: String? = null,
)