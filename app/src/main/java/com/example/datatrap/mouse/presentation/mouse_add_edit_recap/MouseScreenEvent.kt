package com.example.datatrap.mouse.presentation.mouse_add_edit_recap

import com.example.datatrap.settings.protocol.data.ProtocolEntity

sealed interface MouseScreenEvent {
    data class OnSelectProtocol(val protocol: ProtocolEntity): MouseScreenEvent
    object OnProtocolDropDownClick: MouseScreenEvent
    object OnProtocolDropDownDismiss: MouseScreenEvent
}