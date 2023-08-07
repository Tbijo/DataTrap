package com.example.datatrap.settings.protocol.presentation

import com.example.datatrap.settings.protocol.data.Protocol

data class ProtocolListUiState(
    val protocolList: List<Protocol>,
    val protocolName: String? = null,
)