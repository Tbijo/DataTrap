package com.example.datatrap.settings.vegettype.presentation

import com.example.datatrap.settings.vegettype.data.VegetTypeEntity

sealed interface VegetTypeListScreenEvent {
    object OnInsertClick: VegetTypeListScreenEvent
    data class OnItemClick(val vegetTypeEntity: VegetTypeEntity): VegetTypeListScreenEvent
    data class OnDeleteClick(val vegetTypeEntity: VegetTypeEntity): VegetTypeListScreenEvent
    data class OnNameTextChanged(val text: String): VegetTypeListScreenEvent
}