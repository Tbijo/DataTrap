package com.example.datatrap.specie.presentation.specie_add_edit

sealed interface SpecieScreenEvent {
    object OnInsertClick: SpecieScreenEvent
    object OnCameraClick: SpecieScreenEvent

    data class OnSpecieCodeTextChanged(val text: String): SpecieScreenEvent
    data class OnFullNameTextChanged(val text: String): SpecieScreenEvent
    data class OnSynonymTextChanged(val text: String): SpecieScreenEvent
    data class OnAuthorityTextChanged(val text: String): SpecieScreenEvent
    data class OnDescriptionTextChanged(val text: String): SpecieScreenEvent

    object OnIsSmallClick: SpecieScreenEvent
    data class OnNumFingersClick(val numFingers: Int?): SpecieScreenEvent

    data class OnMinWeightTextChanged(val text: String): SpecieScreenEvent
    data class OnMaxWeightTextChanged(val text: String): SpecieScreenEvent
    data class OnBodyLenTextChanged(val text: String): SpecieScreenEvent
    data class OnTailLenTextChanged(val text: String): SpecieScreenEvent
    data class OnMinFeetLenTextChanged(val text: String): SpecieScreenEvent
    data class OnMaxFeetLenTextChanged(val text: String): SpecieScreenEvent
    data class OnNoteTextChanged(val text: String): SpecieScreenEvent

    data class OnReceiveImageName(val imageName: String?, val imageNote: String?): SpecieScreenEvent
}