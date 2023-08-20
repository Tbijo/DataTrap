package com.example.datatrap.settings.methodtype.presentation

import com.example.datatrap.settings.methodtype.data.MethodTypeEntity

data class MethodTypeListUiState(
    val methodTypeEntityList: List<MethodTypeEntity> = emptyList(),
    val methodTypeEntity: MethodTypeEntity? = null,
    val isLoading: Boolean = true,
    val textNameValue: String = "",
    val textNameError: String? = null,
)