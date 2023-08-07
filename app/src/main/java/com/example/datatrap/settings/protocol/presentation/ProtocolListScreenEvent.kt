package com.example.datatrap.settings.protocol.presentation

import com.example.datatrap.settings.protocol.data.Protocol

sealed interface ProtocolListScreenEvent {
    data class OnInsertClick(val protocol: Protocol): ProtocolListScreenEvent
    data class OnItemClick(val protocol: Protocol): ProtocolListScreenEvent
    data class OnDeleteClick(val protocol: Protocol): ProtocolListScreenEvent
}