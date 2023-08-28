package com.example.datatrap.locality.presentation.locality_add_edit

sealed interface LocalityScreenEvent {
    object OnInsertClick: LocalityScreenEvent

    data class OnLocalityNameChange(val text: String): LocalityScreenEvent
}