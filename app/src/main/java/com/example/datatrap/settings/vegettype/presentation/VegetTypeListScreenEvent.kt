package com.example.datatrap.settings.vegettype.presentation

import com.example.datatrap.settings.vegettype.data.VegetType

sealed interface VegetTypeListScreenEvent {
    data class OnInsertClick(val vegetType: VegetType): VegetTypeListScreenEvent
    data class OnItemClick(val vegetType: VegetType): VegetTypeListScreenEvent
    data class OnDeleteClick(val vegetType: VegetType): VegetTypeListScreenEvent
}