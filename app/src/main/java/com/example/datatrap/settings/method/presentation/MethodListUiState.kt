package com.example.datatrap.settings.method.presentation

import com.example.datatrap.settings.method.data.MethodEntity

data class MethodListUiState(
    val methodEntityList: List<MethodEntity>,
    val methodName: String? = null,
)