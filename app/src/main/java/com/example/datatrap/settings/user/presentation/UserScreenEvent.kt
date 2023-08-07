package com.example.datatrap.settings.user.presentation

import com.example.datatrap.settings.user.data.User

sealed interface UserScreenEvent {
    data class OnInsertClick(val user: User): UserScreenEvent
    data class OnItemClick(val user: User): UserScreenEvent
    data class OnDeleteClick(val user: User): UserScreenEvent

    // In bottom sheet
    data class OnSaveUser(val user: User): UserScreenEvent
}