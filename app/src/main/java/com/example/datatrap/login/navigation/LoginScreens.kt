package com.example.datatrap.login.navigation

sealed class LoginScreens(val route: String) {
    object LoginScreen: LoginScreens("login_screen")
}