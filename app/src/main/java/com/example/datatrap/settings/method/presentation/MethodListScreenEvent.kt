package com.example.datatrap.settings.method.presentation

import com.example.datatrap.settings.method.data.Method

sealed interface MethodListScreenEvent {
    data class OnInsertClick(val method: Method): MethodListScreenEvent
    data class OnItemClick(val method: Method): MethodListScreenEvent
    data class OnDeleteClick(val method: Method): MethodListScreenEvent
}