package com.example.datatrap.settings.traptype.presentation

import com.example.datatrap.settings.traptype.data.TrapType

sealed interface TrapTypeListScreenEvent {
    data class OnInsertClick(val trapType: TrapType): TrapTypeListScreenEvent
    data class OnItemClick(val trapType: TrapType): TrapTypeListScreenEvent
    data class OnDeleteClick(val trapType: TrapType): TrapTypeListScreenEvent
}