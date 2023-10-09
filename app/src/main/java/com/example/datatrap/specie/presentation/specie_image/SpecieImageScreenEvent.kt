package com.example.datatrap.specie.presentation.specie_image

import android.net.Uri

sealed interface SpecieImageScreenEvent {
    object OnInsertClick: SpecieImageScreenEvent
    data class OnImageResult(val uri: Uri): SpecieImageScreenEvent
    data class OnNoteTextChanged(val text: String): SpecieImageScreenEvent
}