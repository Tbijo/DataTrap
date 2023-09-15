package com.example.datatrap.about.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.datatrap.about.presentation.AboutScreen

fun NavGraphBuilder.aboutNavigation(navController: NavHostController) {
    composable(
        route = AboutScreens.AboutScreen.route
    ) {
        AboutScreen(
            onEvent = {}
        )
    }
}