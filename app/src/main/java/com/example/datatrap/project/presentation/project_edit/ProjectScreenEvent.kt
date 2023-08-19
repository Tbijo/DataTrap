package com.example.datatrap.project.presentation.project_edit

sealed interface ProjectScreenEvent {

    object OnInsertClick: ProjectScreenEvent

    data class OnProjectNameChange(val text: String): ProjectScreenEvent
    data class OnNumberLocalChange(val text: String): ProjectScreenEvent
    data class OnNumberMiceChange(val text: String): ProjectScreenEvent
}