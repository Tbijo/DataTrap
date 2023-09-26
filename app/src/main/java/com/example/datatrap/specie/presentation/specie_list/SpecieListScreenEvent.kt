package com.example.datatrap.specie.presentation.specie_list

import androidx.compose.ui.focus.FocusState
import com.example.datatrap.core.presentation.components.DrawerScreens
import com.example.datatrap.specie.data.SpecieEntity

sealed interface SpecieListScreenEvent {

    data class OnItemClick(val specieEntity: SpecieEntity): SpecieListScreenEvent
    data class OnDeleteClick(val specieEntity: SpecieEntity): SpecieListScreenEvent
    object OnAddButtonClick: SpecieListScreenEvent
    data class OnDrawerItemClick(val drawerScreen: DrawerScreens): SpecieListScreenEvent

    data class OnSearchTextChange(val text: String): SpecieListScreenEvent
    data class ChangeTitleFocus(val focusState: FocusState): SpecieListScreenEvent

}