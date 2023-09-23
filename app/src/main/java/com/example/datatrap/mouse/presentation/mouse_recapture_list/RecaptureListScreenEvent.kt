package com.example.datatrap.mouse.presentation.mouse_recapture_list

import com.example.datatrap.core.util.EnumMouseAge
import com.example.datatrap.core.util.EnumSex
import com.example.datatrap.mouse.domain.model.MouseRecapList
import com.example.datatrap.specie.data.SpecieEntity
import java.time.LocalDate

sealed interface RecaptureListScreenEvent {
    data class OnItemClick(val mouse: MouseRecapList): RecaptureListScreenEvent
    object OnSearchButtonClick: RecaptureListScreenEvent

    data class OnCodeTextChanged(val text: String): RecaptureListScreenEvent

    object OnSpecieDismiss: RecaptureListScreenEvent
    object OnSpecieDropDownClick: RecaptureListScreenEvent
    data class OnSpecieSelect(val specieEntity: SpecieEntity): RecaptureListScreenEvent

    data class OnSexClick(val sex: EnumSex?): RecaptureListScreenEvent
    data class OnAgeClick(val age: EnumMouseAge?): RecaptureListScreenEvent
    data class OnSexActiveClick(val sexActive: Boolean): RecaptureListScreenEvent
    data class OnLactatingClick(val lactating: Boolean): RecaptureListScreenEvent
    data class OnGravidityClick(val gravidity: Boolean): RecaptureListScreenEvent
    data class OnSelectFromDate(val fromDate: LocalDate): RecaptureListScreenEvent
    data class OnSelectToDate(val toDate: LocalDate): RecaptureListScreenEvent
    object OnConfirmClick: RecaptureListScreenEvent
}