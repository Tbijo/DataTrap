package com.example.datatrap.settings.method.presentation

import com.example.datatrap.settings.method.data.MethodEntity

sealed interface MethodListScreenEvent {
    object OnInsertClick: MethodListScreenEvent
    data class OnItemClick(val methodEntity: MethodEntity): MethodListScreenEvent
    data class OnDeleteClick(val methodEntity: MethodEntity): MethodListScreenEvent
    data class OnNameTextChanged(val text: String): MethodListScreenEvent
}