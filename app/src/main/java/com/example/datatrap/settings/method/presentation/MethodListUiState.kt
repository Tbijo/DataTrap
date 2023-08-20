package com.example.datatrap.settings.method.presentation

import com.example.datatrap.settings.method.data.MethodEntity

data class MethodListUiState(
    val methodEntityList: List<MethodEntity> = emptyList(),
    val methodEntity: MethodEntity? = null,
    val isLoading: Boolean = true,
    val textNameValue: String = "",
    val textNameError: String? = null,
)