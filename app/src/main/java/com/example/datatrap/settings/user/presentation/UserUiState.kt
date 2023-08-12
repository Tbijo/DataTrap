package com.example.datatrap.settings.user.presentation

import com.example.datatrap.settings.user.data.UserEntity

data class UserUiState(
    val userEntityList: List<UserEntity>,
    val selectedUserEntity: UserEntity? = null,
)