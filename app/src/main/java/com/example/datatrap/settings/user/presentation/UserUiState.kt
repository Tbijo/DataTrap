package com.example.datatrap.settings.user.presentation

import com.example.datatrap.settings.user.data.User

data class UserUiState(
    val userList: List<User>,
    val selectedUser: User? = null,
)