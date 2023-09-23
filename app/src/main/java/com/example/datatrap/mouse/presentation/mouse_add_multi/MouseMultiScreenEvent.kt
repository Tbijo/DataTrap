package com.example.datatrap.mouse.presentation.mouse_add_multi

import com.example.datatrap.specie.data.SpecieEntity

sealed interface MouseMultiScreenEvent {
    object OnAddRowClick: MouseMultiScreenEvent
    object OnRemoveRowClick: MouseMultiScreenEvent
    object OnInsertClick: MouseMultiScreenEvent

    data class OnTrapIdDismissClick(val index: Int): MouseMultiScreenEvent
    data class OnSpecieDismissClick(val index: Int): MouseMultiScreenEvent

    data class OnTrapIdClick(val index: Int, val trapId: Int): MouseMultiScreenEvent
    data class OnSpecieClick(val index: Int, val specie: SpecieEntity): MouseMultiScreenEvent
}