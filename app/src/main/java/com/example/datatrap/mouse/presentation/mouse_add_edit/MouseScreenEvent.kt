package com.example.datatrap.mouse.presentation.mouse_add_edit

import com.example.datatrap.core.util.EnumCaptureID
import com.example.datatrap.core.util.EnumMouseAge
import com.example.datatrap.core.util.EnumSex
import com.example.datatrap.settings.data.protocol.ProtocolEntity
import com.example.datatrap.specie.data.SpecieEntity

sealed interface MouseScreenEvent {
    object OnInsertClick: MouseScreenEvent
    data class OnCameraClick(val mouseId: String?): MouseScreenEvent
    object OnMouseClick: MouseScreenEvent
    object OnGenerateButtonClick: MouseScreenEvent

    object OnLeave: MouseScreenEvent

    data class OnReceiveImageName(val imageName: String?, val imageNote: String?): MouseScreenEvent

    // verify dialog
    object OnDialogOkClick: MouseScreenEvent
    object OnDialogCancelClick: MouseScreenEvent
    object OnDialogDismiss: MouseScreenEvent

    data class OnCodeTextChanged(val text: String): MouseScreenEvent

    data class OnSelectProtocol(val protocol: ProtocolEntity): MouseScreenEvent
    object OnProtocolDropDownClick: MouseScreenEvent
    object OnProtocolDropDownDismiss: MouseScreenEvent

    data class OnSelectSpecie(val specieEntity: SpecieEntity): MouseScreenEvent
    object OnSpecieDropDownClick: MouseScreenEvent
    object OnSpecieDropDownDismiss: MouseScreenEvent

    data class OnSelectTrapID(val trapID: Int): MouseScreenEvent
    object OnTrapIDDropDownClick: MouseScreenEvent
    object OnTrapIDDropDownDismiss: MouseScreenEvent

    data class OnCaptureIdClick(val captureID: EnumCaptureID?): MouseScreenEvent
    data class OnSexClick(val sex: EnumSex?): MouseScreenEvent
    data class OnAgeClick(val age: EnumMouseAge?): MouseScreenEvent

    object OnSexActiveClick: MouseScreenEvent
    object OnGravidityClick: MouseScreenEvent
    object OnLactatingClick: MouseScreenEvent

    data class OnBodyTextChanged(val text: String): MouseScreenEvent
    data class OnTailTextChanged(val text: String): MouseScreenEvent
    data class OnFeetTextChanged(val text: String): MouseScreenEvent
    data class OnEarTextChanged(val text: String): MouseScreenEvent

    data class OnWeightTextChanged(val text: String): MouseScreenEvent
    data class OnTestesLengthTextChanged(val text: String): MouseScreenEvent
    data class OnTestesWidthTextChanged(val text: String): MouseScreenEvent

    data class OnRightEmbryoTextChanged(val text: String): MouseScreenEvent
    data class OnLeftEmbryoTextChanged(val text: String): MouseScreenEvent
    data class OnEmbryoDiameterTextChanged(val text: String): MouseScreenEvent

    data class OnMcRightTextChanged(val text: String): MouseScreenEvent
    data class OnMcLeftTextChanged(val text: String): MouseScreenEvent
    object OnMcClick: MouseScreenEvent
    data class OnNoteTextChanged(val text: String): MouseScreenEvent
}