package com.example.datatrap.settings.methodtype.presentation

import com.example.datatrap.settings.methodtype.data.MethodType

sealed interface MethodTypeListScreenEvent {
    data class OnInsertClick(val methodType: MethodType): MethodTypeListScreenEvent
    data class OnItemClick(val methodType: MethodType): MethodTypeListScreenEvent
    data class OnDeleteClick(val methodType: MethodType): MethodTypeListScreenEvent
}