package com.example.datatrap.settings.envtype.presentation

import com.example.datatrap.settings.envtype.data.EnvTypeEntity

sealed interface EnvTypeListScreenEvent {
    data class OnInsertClick(val envTypeEntity: EnvTypeEntity): EnvTypeListScreenEvent
    data class OnItemClick(val envTypeEntity: EnvTypeEntity): EnvTypeListScreenEvent
    data class OnDeleteClick(val envTypeEntity: EnvTypeEntity): EnvTypeListScreenEvent
}