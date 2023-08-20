package com.example.datatrap.settings.envtype.presentation

import com.example.datatrap.settings.envtype.data.EnvTypeEntity

sealed interface EnvTypeListScreenEvent {
    object OnInsertClick: EnvTypeListScreenEvent
    data class OnItemClick(val envTypeEntity: EnvTypeEntity): EnvTypeListScreenEvent
    data class OnDeleteClick(val envTypeEntity: EnvTypeEntity): EnvTypeListScreenEvent
    data class OnNameTextChanged(val text: String): EnvTypeListScreenEvent
}