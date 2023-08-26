package com.example.datatrap.locality.presentation.locality_list

import com.example.datatrap.locality.data.LocalityEntity

sealed interface LocalityListScreenEvent {
    data class OnDeleteClick(val localityEntity: LocalityEntity): LocalityListScreenEvent
    data class OnItemClick(val localityEntity: LocalityEntity): LocalityListScreenEvent
    data class OnUpdateButtonClick(val localityEntity: LocalityEntity): LocalityListScreenEvent
    object OnAddButtonClick: LocalityListScreenEvent
}