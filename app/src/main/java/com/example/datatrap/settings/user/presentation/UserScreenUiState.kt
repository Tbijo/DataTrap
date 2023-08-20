package com.example.datatrap.settings.user.presentation

import com.example.datatrap.settings.user.data.UserEntity

data class UserScreenUiState(
    val userEntityList: List<UserEntity> = emptyList(),
    val selectedUserEntity: UserEntity? = null,
    val isLoading: Boolean = true,

    val textNameValue: String = "",
    val textNameError: String? = null,

    val textPasswordValue: String = "",
    val textPasswordError: String? = null,
)