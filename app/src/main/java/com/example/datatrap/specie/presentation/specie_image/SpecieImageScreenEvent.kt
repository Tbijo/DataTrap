package com.example.datatrap.specie.presentation.specie_image

import android.net.Uri

sealed interface SpecieImageScreenEvent {
    data class OnLeave(val makeChange: Boolean): SpecieImageScreenEvent
    data class OnImageResult(val uri: Uri): SpecieImageScreenEvent
    data class OnNoteTextChanged(val text: String): SpecieImageScreenEvent
}