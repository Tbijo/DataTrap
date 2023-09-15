package com.example.datatrap.settings.methodtype.presentation

import com.example.datatrap.settings.methodtype.data.MethodTypeEntity

data class MethodTypeListUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val methodTypeEntityList: List<MethodTypeEntity> = emptyList(),
    val methodTypeEntity: MethodTypeEntity? = null,
    val textNameValue: String = "",
    val textNameError: String? = null,
)