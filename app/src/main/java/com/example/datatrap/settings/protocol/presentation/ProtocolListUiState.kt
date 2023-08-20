package com.example.datatrap.settings.protocol.presentation

import com.example.datatrap.settings.protocol.data.ProtocolEntity

data class ProtocolListUiState(
    val protocolEntityList: List<ProtocolEntity> = emptyList(),
    val protocolEntity: ProtocolEntity? = null,
    val isLoading: Boolean = true,
    val textNameValue: String = "",
    val textNameError: String? = null,
)