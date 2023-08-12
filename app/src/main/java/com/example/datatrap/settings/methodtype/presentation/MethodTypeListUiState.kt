package com.example.datatrap.settings.methodtype.presentation

import com.example.datatrap.settings.methodtype.data.MethodTypeEntity

data class MethodTypeListUiState(
    val methodTypeEntityList: List<MethodTypeEntity>,
    val methodTypeName: String? = null,
)