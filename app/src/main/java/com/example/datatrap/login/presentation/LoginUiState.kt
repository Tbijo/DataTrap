package com.example.datatrap.login.presentation

import com.example.datatrap.core.util.ScienceTeam

data class LoginUiState(
    val error: String? = null,

    val userName: String = "",
    val userNameError: String? = null,

    val password: String = "",
    val passwordError: String? = null,

    val selectedTeam: ScienceTeam? = null,
)