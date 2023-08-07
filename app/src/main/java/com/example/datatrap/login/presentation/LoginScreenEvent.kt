package com.example.datatrap.login.presentation

import com.example.datatrap.core.util.EnumTeam

sealed interface LoginScreenEvent {
    data class OnUserNameChanged(val value: String): LoginScreenEvent
    data class OnPasswordChanged(val value: String): LoginScreenEvent
    data class OnSelectTeam(val team: EnumTeam): LoginScreenEvent
    object LogIn: LoginScreenEvent
}