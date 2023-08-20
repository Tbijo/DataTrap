package com.example.datatrap.settings.user.presentation

import com.example.datatrap.settings.user.data.UserEntity

sealed interface UserScreenEvent {
    object OnInsertClick: UserScreenEvent
    data class OnItemClick(val userEntity: UserEntity): UserScreenEvent
    data class OnDeleteClick(val userEntity: UserEntity): UserScreenEvent
    data class OnNameTextChanged(val text: String): UserScreenEvent
    data class OnPasswordChanged(val text: String): UserScreenEvent
}