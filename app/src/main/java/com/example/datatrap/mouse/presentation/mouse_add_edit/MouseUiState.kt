package com.example.datatrap.mouse.presentation.mouse_add_edit

import com.example.datatrap.core.util.EnumCaptureID
import com.example.datatrap.core.util.EnumMouseAge
import com.example.datatrap.core.util.EnumSex
import com.example.datatrap.mouse.data.MouseEntity
import com.example.datatrap.settings.protocol.data.ProtocolEntity
import com.example.datatrap.specie.data.SpecieEntity

data class MouseUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val mouseEntity: MouseEntity? = null,
    val occupiedTrapIdList: List<Int> = emptyList(),

    val isSheetExpanded: Boolean = false,

    // verify dialog
    val isDialogShowing: Boolean = false,
    val dialogTitle: String = "",
    val dialogMessage: String = "",
    val isMouseOkay: Boolean = true,

    val code: String = "",
    val codeError: String? = null,

    val specieList: List<SpecieEntity> = emptyList(),
    val specieEntity: SpecieEntity? = null,
    val isSpecieExpanded: Boolean = false,

    val protocolList: List<ProtocolEntity> = emptyList(),
    val protocolEntity: ProtocolEntity? = null,
    val isProtocolExpanded: Boolean = false,

    val trapIDList: List<Int> = emptyList(),
    val trapID: Int? = null,
    val isTrapIDExpanded: Boolean = false,

    val captureID: EnumCaptureID? = null,
    val sex: EnumSex? = null,
    val age: EnumMouseAge? = null,

    val sexActive: Boolean = false,
    val gravidity: Boolean = false,
    val lactating: Boolean = false,

    val body: String = "",
    val bodyError: String? = null,
    val tail: String = "",
    val tailError: String? = null,
    val feet: String = "",
    val feetError: String? = null,
    val ear: String = "",
    val earError: String? = null,

    val weight: String = "",
    val weightError: String? = null,
    val testesLength: String = "",
    val testesLengthError: String? = null,
    val testesWidth: String = "",
    val testesWidthError: String? = null,

    val rightEmbryo: String = "",
    val rightEmbryoError: String? = null,
    val leftEmbryo: String = "",
    val leftEmbryoError: String? = null,
    val embryoDiameter: String = "",
    val embryoDiameterError: String? = null,

    val mcRight: String = "",
    val mcRightError: String? = null,
    val mcLeft: String = "",
    val mcLeftError: String? = null,
    val mc: Boolean = false,

    val note: String = "",
    val noteError: String? = null,
)