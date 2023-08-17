package com.example.datatrap.session.navigation

sealed class SessionScreens(val route: String) {
    object SessionListScreen: SessionScreens("session_list_screen")
    object SessionScreen: SessionScreens("session_screen")
}