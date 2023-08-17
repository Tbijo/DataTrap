package com.example.datatrap.mouse.navigation

sealed class MouseScreens(val route: String) {

    object MouseListScreen: MouseScreens("mouse_list_screen")

    object MouseScreen: MouseScreens("mouse_screen")

}