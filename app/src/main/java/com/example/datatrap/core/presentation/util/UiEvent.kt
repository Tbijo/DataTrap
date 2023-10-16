package com.example.datatrap.core.presentation.util

sealed interface UiEvent {
    object NavigateBack: UiEvent
    object NavigateNext: UiEvent
}