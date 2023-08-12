package com.example.datatrap.settings.user.presentation

import com.example.datatrap.settings.user.data.UserEntity

sealed interface UserScreenEvent {
    data class OnInsertClick(val userEntity: UserEntity): UserScreenEvent
    data class OnItemClick(val userEntity: UserEntity): UserScreenEvent
    data class OnDeleteClick(val userEntity: UserEntity): UserScreenEvent

    // In bottom sheet
    data class OnSaveUser(val userEntity: UserEntity): UserScreenEvent
}