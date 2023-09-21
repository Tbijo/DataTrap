package com.example.datatrap.mouse.presentation.mouse_list

sealed interface MouseListScreenEvent {
    data class OnItemClick(val mouseId: String): MouseListScreenEvent
    data class OnUpdateButtonClick(val mouseId: String): MouseListScreenEvent
    data class OnDeleteClick(val mouseId: String): MouseListScreenEvent
    object OnAddButtonClick: MouseListScreenEvent

    object OnRecaptureButtonClick: MouseListScreenEvent
    object OnMultiButtonClick: MouseListScreenEvent
}