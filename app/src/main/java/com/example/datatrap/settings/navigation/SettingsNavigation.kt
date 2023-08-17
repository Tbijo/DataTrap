package com.example.datatrap.settings.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.datatrap.settings.SettingsListScreen

fun NavGraphBuilder.settingsNavigation(navController: NavHostController) {

    composable(
        route = SettingsScreens.SettingsListScreen.route
    ) {
        SettingsListScreen(
            onEvent = {}
        )
    }

}