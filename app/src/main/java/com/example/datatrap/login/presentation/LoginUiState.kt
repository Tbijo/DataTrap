package com.example.datatrap.login.presentation

import com.example.datatrap.core.util.EnumTeam

data class LoginUiState(
    val userName: String? = null,
    val password: String? = null,
    val userNameError: String? = null,
    val passwordError: String? = null,
    val selectedTeam: EnumTeam? = null,
)