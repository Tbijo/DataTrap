package com.example.datatrap.settings.method.presentation

import com.example.datatrap.settings.method.data.MethodEntity

data class MethodListUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val methodEntityList: List<MethodEntity> = emptyList(),
    val methodEntity: MethodEntity? = null,
    val textNameValue: String = "",
    val textNameError: String? = null,
)