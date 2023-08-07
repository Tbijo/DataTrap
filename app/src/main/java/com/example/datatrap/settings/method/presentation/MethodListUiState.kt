package com.example.datatrap.settings.method.presentation

import com.example.datatrap.settings.method.data.Method

data class MethodListUiState(
    val methodList: List<Method>,
    val methodName: String? = null,
)