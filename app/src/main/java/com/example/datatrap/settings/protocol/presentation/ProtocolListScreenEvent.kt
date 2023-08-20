package com.example.datatrap.settings.protocol.presentation

import com.example.datatrap.settings.protocol.data.ProtocolEntity

sealed interface ProtocolListScreenEvent {
    object OnInsertClick: ProtocolListScreenEvent
    data class OnItemClick(val protocolEntity: ProtocolEntity): ProtocolListScreenEvent
    data class OnDeleteClick(val protocolEntity: ProtocolEntity): ProtocolListScreenEvent
    data class OnNameTextChanged(val text: String): ProtocolListScreenEvent
}