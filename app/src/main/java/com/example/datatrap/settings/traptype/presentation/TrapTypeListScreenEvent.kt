package com.example.datatrap.settings.traptype.presentation

import com.example.datatrap.settings.traptype.data.TrapTypeEntity

sealed interface TrapTypeListScreenEvent {
    data class OnInsertClick(val trapTypeEntity: TrapTypeEntity): TrapTypeListScreenEvent
    data class OnItemClick(val trapTypeEntity: TrapTypeEntity): TrapTypeListScreenEvent
    data class OnDeleteClick(val trapTypeEntity: TrapTypeEntity): TrapTypeListScreenEvent
}