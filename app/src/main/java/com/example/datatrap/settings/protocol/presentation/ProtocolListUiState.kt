package com.example.datatrap.settings.protocol.presentation

import com.example.datatrap.settings.protocol.data.ProtocolEntity

data class ProtocolListUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val protocolEntityList: List<ProtocolEntity> = emptyList(),
    val protocolEntity: ProtocolEntity? = null,
    val textNameValue: String = "",
    val textNameError: String? = null,
)