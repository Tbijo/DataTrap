package com.example.datatrap.core.presentation.util

sealed interface UiEvent {

    // navigate back
    object NavigateBack: UiEvent
}