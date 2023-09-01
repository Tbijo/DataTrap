package com.example.datatrap.login.presentation

import com.example.datatrap.core.util.ScienceTeam

sealed interface LoginScreenEvent {
    data class OnUserNameChanged(val text: String): LoginScreenEvent
    data class OnPasswordChanged(val text: String): LoginScreenEvent
    data class OnSelectTeam(val team: ScienceTeam): LoginScreenEvent
    object LogIn: LoginScreenEvent
}