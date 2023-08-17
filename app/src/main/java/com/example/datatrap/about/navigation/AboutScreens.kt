package com.example.datatrap.about.navigation

sealed class AboutScreens(val route: String) {
    object AboutScreen: AboutScreens("about_screen")
}