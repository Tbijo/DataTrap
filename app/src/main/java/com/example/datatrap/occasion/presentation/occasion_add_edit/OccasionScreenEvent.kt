package com.example.datatrap.occasion.presentation.occasion_add_edit

import com.example.datatrap.settings.envtype.data.EnvTypeEntity
import com.example.datatrap.settings.method.data.MethodEntity
import com.example.datatrap.settings.methodtype.data.MethodTypeEntity
import com.example.datatrap.settings.traptype.data.TrapTypeEntity
import com.example.datatrap.settings.vegettype.data.VegetTypeEntity

sealed interface OccasionScreenEvent {
    object OnInsertClick: OccasionScreenEvent
    object OnCameraClick: OccasionScreenEvent
    object OnCloudClick: OccasionScreenEvent

    data class OnSelectEnvType(val envType: EnvTypeEntity): OccasionScreenEvent
    object OnEnvTypeDropDownClick: OccasionScreenEvent
    object OnEnvTypeDropDownDismiss: OccasionScreenEvent

    data class OnSelectMethod(val method: MethodEntity): OccasionScreenEvent
    object OnMethodDropDownClick: OccasionScreenEvent
    object OnMethodDropDownDismiss: OccasionScreenEvent

    data class OnSelectMethodType(val methodType: MethodTypeEntity): OccasionScreenEvent
    object OnMethodTypeDropDownClick: OccasionScreenEvent
    object OnMethodTypeDropDownDismiss: OccasionScreenEvent

    data class OnSelectTrapType(val trapType: TrapTypeEntity): OccasionScreenEvent
    object OnTrapTypeDropDownClick: OccasionScreenEvent
    object OnTrapTypeDropDownDismiss: OccasionScreenEvent

    data class OnSelectVegType(val vegType: VegetTypeEntity): OccasionScreenEvent
    object OnVegTypeDropDownClick: OccasionScreenEvent
    object OnVegTypeDropDownDismiss: OccasionScreenEvent

    object OnGotCaughtClick: OccasionScreenEvent

    data class OnNumberOfTrapsChanged(val text: String): OccasionScreenEvent
    data class OnNumberOfMiceChanged(val text: String): OccasionScreenEvent
    data class OnWeatherChanged(val text: String): OccasionScreenEvent
    data class OnTemperatureChanged(val text: String): OccasionScreenEvent
    data class OnLegitimationChanged(val text: String): OccasionScreenEvent
    data class OnNoteChanged(val text: String): OccasionScreenEvent
}