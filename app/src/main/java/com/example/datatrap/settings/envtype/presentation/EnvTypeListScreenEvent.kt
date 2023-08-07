package com.example.datatrap.settings.envtype.presentation

import com.example.datatrap.settings.envtype.data.EnvType

sealed interface EnvTypeListScreenEvent {
    data class OnInsertClick(val envType: EnvType): EnvTypeListScreenEvent
    data class OnItemClick(val envType: EnvType): EnvTypeListScreenEvent
    data class OnDeleteClick(val envType: EnvType): EnvTypeListScreenEvent
}