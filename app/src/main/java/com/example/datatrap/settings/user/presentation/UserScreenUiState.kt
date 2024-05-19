package com.example.datatrap.settings.user.presentation

import com.example.datatrap.settings.user.data.UserEntity

data class UserScreenUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val userEntityList: List<UserEntity> = emptyList(),

    val textNameValue: String = "",
    val textNameError: String? = null,

    val textPasswordValue: String = "",
    val textPasswordError: String? = null,
)