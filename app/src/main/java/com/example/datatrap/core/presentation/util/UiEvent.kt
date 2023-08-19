package com.example.datatrap.core.presentation.util

sealed interface UiEvent {

    // show snackbar
    data class ShowSnackbar(val message: String): UiEvent

    // navigate back
    object Navigate: UiEvent
}