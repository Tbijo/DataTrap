package com.example.datatrap.settings.envtype.presentation

import com.example.datatrap.settings.envtype.data.EnvType

data class EnvTypeListUiState(
    val envTypeList: List<EnvType>,
    val envTypeName: String? = null,
)