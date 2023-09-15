package com.example.datatrap.mouse.presentation.mouse_add_edit_recap

import com.example.datatrap.settings.protocol.data.ProtocolEntity

data class MouseUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val protocolList: List<ProtocolEntity> = emptyList(),
    val protocolEntity: ProtocolEntity? = null,
    val isProtocolExpanded: Boolean = false,
)
