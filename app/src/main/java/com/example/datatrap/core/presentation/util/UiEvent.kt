package com.example.datatrap.core.presentation.util

sealed interface UiEvent {
    data object NavigateBack: UiEvent
    data object NavigateNext: UiEvent
}