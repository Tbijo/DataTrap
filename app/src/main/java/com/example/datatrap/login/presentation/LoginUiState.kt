package com.example.datatrap.login.presentation

import com.example.datatrap.core.util.ScienceTeam

data class LoginUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val userName: String = "",
    val userNameError: String? = null,

    val password: String = "",
    val passwordError: String? = null,

    val selectedTeam: ScienceTeam? = null,

    // We want to show multiple dialogs (one after the other) because the user may decline all of the permissions
    // We need to queue these dialogs, queue data structure
    // String will be the permission
    val visiblePermissionDialogQueue: MutableList<String> = mutableListOf(),
)