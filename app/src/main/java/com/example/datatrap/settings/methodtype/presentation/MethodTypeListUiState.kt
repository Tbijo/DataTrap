package com.example.datatrap.settings.methodtype.presentation

import com.example.datatrap.settings.methodtype.data.MethodType

data class MethodTypeListUiState(
    val methodTypeList: List<MethodType>,
    val methodTypeName: String? = null,
)