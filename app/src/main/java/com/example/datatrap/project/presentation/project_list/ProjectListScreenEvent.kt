package com.example.datatrap.project.presentation.project_list

import androidx.compose.ui.focus.FocusState
import com.example.datatrap.core.presentation.components.DrawerScreens
import com.example.datatrap.project.data.ProjectEntity

sealed interface ProjectListScreenEvent {
    data class OnDeleteClick(val projectEntity: ProjectEntity): ProjectListScreenEvent
    data class OnItemClick(val projectEntity: ProjectEntity): ProjectListScreenEvent
    data class OnUpdateButtonClick(val projectEntity: ProjectEntity): ProjectListScreenEvent
    data class OnDrawerItemClick(val drawerScreen: DrawerScreens): ProjectListScreenEvent
    object OnAddButtonClick: ProjectListScreenEvent
    object OnLogoutButtonClick: ProjectListScreenEvent
    data class OnSearchTextChange(val text: String): ProjectListScreenEvent

    /** Click on textfield to hide the hint */
    data class ChangeTitleFocus(val focusState: FocusState): ProjectListScreenEvent
}