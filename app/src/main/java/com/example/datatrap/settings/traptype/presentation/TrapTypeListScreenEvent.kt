package com.example.datatrap.settings.traptype.presentation

import com.example.datatrap.settings.traptype.data.TrapTypeEntity

sealed interface TrapTypeListScreenEvent {
    object OnInsertClick: TrapTypeListScreenEvent
    data class OnItemClick(val trapTypeEntity: TrapTypeEntity): TrapTypeListScreenEvent
    data class OnDeleteClick(val trapTypeEntity: TrapTypeEntity): TrapTypeListScreenEvent
    data class OnNameTextChanged(val text: String): TrapTypeListScreenEvent
}