package com.example.datatrap.locality.presentation.locality_add_edit

sealed interface LocalityScreenEvent {
    object OnInsertClick: LocalityScreenEvent
    data class OnLocalityNameChange(val text: String): LocalityScreenEvent
    data class OnNumSessionsChange(val text: String): LocalityScreenEvent
    data class OnNoteChange(val text: String): LocalityScreenEvent

    object OnButtonCoorAClick: LocalityScreenEvent
    object OnButtonCoorBClick: LocalityScreenEvent

    data class OnLatitudeAChange(val text: String): LocalityScreenEvent
    data class OnLongitudeAChange(val text: String): LocalityScreenEvent
    data class OnLatitudeBChange(val text: String): LocalityScreenEvent
    data class OnLongitudeBChange(val text: String): LocalityScreenEvent
}