package com.example.datatrap.settings.vegettype.presentation

import com.example.datatrap.settings.vegettype.data.VegetTypeEntity

sealed interface VegetTypeListScreenEvent {
    data class OnInsertClick(val vegetTypeEntity: VegetTypeEntity): VegetTypeListScreenEvent
    data class OnItemClick(val vegetTypeEntity: VegetTypeEntity): VegetTypeListScreenEvent
    data class OnDeleteClick(val vegetTypeEntity: VegetTypeEntity): VegetTypeListScreenEvent
}