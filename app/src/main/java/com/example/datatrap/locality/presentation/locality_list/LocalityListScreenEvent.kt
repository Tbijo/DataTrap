package com.example.datatrap.locality.presentation.locality_list

import androidx.compose.ui.focus.FocusState
import com.example.datatrap.locality.data.locality.LocalityEntity

sealed interface LocalityListScreenEvent {
    data class OnDeleteClick(val localityEntity: LocalityEntity): LocalityListScreenEvent
    data class OnItemClick(val localityEntity: LocalityEntity): LocalityListScreenEvent
    data class OnUpdateButtonClick(val localityEntity: LocalityEntity): LocalityListScreenEvent
    object OnAddButtonClick: LocalityListScreenEvent

    object OnMapButtonCLick: LocalityListScreenEvent

    data class OnSearchTextChange(val text: String): LocalityListScreenEvent
    data class ChangeTitleFocus(val focusState: FocusState): LocalityListScreenEvent

    data class OnPermissionResult(val permission: String, val isGranted: Boolean): LocalityListScreenEvent
    object OnDismissDialog: LocalityListScreenEvent
}