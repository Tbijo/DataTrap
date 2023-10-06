package com.example.datatrap.specie.presentation.specie_image

sealed interface SpecieImageScreenEvent {
    object OnInsertClick: SpecieImageScreenEvent
    object OnGetImageClick: SpecieImageScreenEvent
    data class OnNoteTextChanged(val text: String): SpecieImageScreenEvent
}