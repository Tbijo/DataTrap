package com.example.datatrap.settings.traptype.presentation

import com.example.datatrap.settings.traptype.data.TrapType

data class TrapTypeListUiState(
    val trapTypeList: List<TrapType>,
    val trapTypeName: String? = null,
)