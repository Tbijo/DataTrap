package com.example.datatrap.settings.protocol.presentation

import com.example.datatrap.settings.protocol.data.ProtocolEntity

data class ProtocolListUiState(
    val protocolEntityList: List<ProtocolEntity>,
    val protocolName: String? = null,
)