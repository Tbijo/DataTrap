package com.example.datatrap.settings.method.presentation

import com.example.datatrap.settings.method.data.MethodEntity

sealed interface MethodListScreenEvent {
    data class OnInsertClick(val methodEntity: MethodEntity): MethodListScreenEvent
    data class OnItemClick(val methodEntity: MethodEntity): MethodListScreenEvent
    data class OnDeleteClick(val methodEntity: MethodEntity): MethodListScreenEvent
}