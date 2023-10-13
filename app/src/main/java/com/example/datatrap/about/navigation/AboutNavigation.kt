package com.example.datatrap.about.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.datatrap.about.presentation.AboutScreen
import com.example.datatrap.about.presentation.AboutUiState

fun NavGraphBuilder.aboutNavigation(navController: NavHostController) {
    composable(
        route = AboutScreens.AboutScreen.route
    ) {
        AboutScreen(
            onEvent = {

            },
            state = AboutUiState(
                isLoading = false
            ),
        )
    }
}