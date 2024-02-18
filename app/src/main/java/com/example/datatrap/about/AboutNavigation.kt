package com.example.datatrap.about

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.datatrap.about.presentation.AboutScreen
import com.example.datatrap.about.presentation.AboutUiState
import com.example.datatrap.core.presentation.components.DrawerScreens
import com.example.datatrap.project.navigateToProjectListScreen
import com.example.datatrap.settings.navigateToSettingsScreen
import com.example.datatrap.specie.navigateToSpecieListScreen
import com.example.datatrap.sync.navigateToSyncScreen

private const val ABOUT_SCREEN_ROUTE = "about_screen"

fun NavController.navigateToAboutScreen() = navigate(ABOUT_SCREEN_ROUTE)

fun NavGraphBuilder.aboutNavigation(navController: NavHostController) {
    composable(route = ABOUT_SCREEN_ROUTE) {
        AboutScreen(
            onEvent = { event ->
                when(event) {
                    DrawerScreens.SPECIES -> {
                        navController.navigateToSpecieListScreen()
                    }
                    DrawerScreens.SETTINGS -> {
                        navController.navigateToSettingsScreen()
                    }
                    DrawerScreens.PROJECTS -> {
                        navController.navigateToProjectListScreen()
                    }
                    DrawerScreens.SYNCHRONIZE -> {
                        navController.navigateToSyncScreen()
                    }
                    else -> Unit
                }
            },
            state = AboutUiState(
                isLoading = false,
            ),
        )
    }
}