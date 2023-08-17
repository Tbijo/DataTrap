package com.example.datatrap.mouse.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.datatrap.mouse.presentation.mouse_add_edit_recap.MouseScreen
import com.example.datatrap.mouse.presentation.mouse_list.MouseListScreen

fun NavGraphBuilder.mouseNavigation(navController: NavHostController) {

    composable(
        route = MouseScreens.MouseListScreen.route
    ) {
        MouseListScreen(
            onEvent = {},
        )
    }

    composable(
        route = MouseScreens.MouseScreen.route
    ) {
        MouseScreen(
            onEvent = {},
        )
    }

}