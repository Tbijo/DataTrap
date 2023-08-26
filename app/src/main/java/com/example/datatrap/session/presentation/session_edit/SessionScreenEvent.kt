package com.example.datatrap.session.presentation.session_edit

sealed interface SessionScreenEvent {
    object OnInsertClick: SessionScreenEvent

    data class OnSessionNumberChange(val text: String): SessionScreenEvent
    data class OnNumberOccasionChange(val text: String): SessionScreenEvent
}