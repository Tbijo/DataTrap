package com.example.datatrap.settings.methodtype.presentation

import com.example.datatrap.settings.methodtype.data.MethodTypeEntity

sealed interface MethodTypeListScreenEvent {
    data class OnInsertClick(val methodTypeEntity: MethodTypeEntity): MethodTypeListScreenEvent
    data class OnItemClick(val methodTypeEntity: MethodTypeEntity): MethodTypeListScreenEvent
    data class OnDeleteClick(val methodTypeEntity: MethodTypeEntity): MethodTypeListScreenEvent
}