package com.example.datatrap.login.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.datatrap.login.presentation.LoginScreen

fun NavGraphBuilder.loginNavigation(navController: NavHostController) {
    composable(
        route = LoginScreens.LoginScreen.route
    ) {
        LoginScreen(
            onEvent = {
                // Navigate to ProjectListScreen
            }
        )
    }
}