package com.example.datatrap.settings.traptype.presentation

import com.example.datatrap.settings.traptype.data.TrapTypeEntity

data class TrapTypeListUiState(
    val trapTypeEntityList: List<TrapTypeEntity>,
    val trapTypeName: String? = null,
)