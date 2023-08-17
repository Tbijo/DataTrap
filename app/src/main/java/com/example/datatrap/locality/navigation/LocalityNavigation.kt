package com.example.datatrap.locality.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.datatrap.locality.presentation.locality_add_edit.LocalityScreen
import com.example.datatrap.locality.presentation.locality_list.LocalityListScreen
import com.example.datatrap.project.navigation.ProjectScreens

fun NavGraphBuilder.localityNavigation(navController: NavHostController) {

    composable(
        route = LocalityScreens.LocalityListScreen.route,
        arguments = listOf(
            navArgument(ProjectScreens.ProjectScreen.projectIdKey) {
                type = NavType.StringType
                defaultValue = null
            },
        ),
    ) {
        LocalityListScreen(
            onEvent = {}
        )
    }

    composable(
        route = LocalityScreens.LocalityScreen.route
    ) {
        LocalityScreen(
            onEvent = {}
        )
    }

    composable(
        route = LocalityScreens.LocalityMapScreen.route
    ) {
//            LocalityMapScreen(
//                onEvent = {}
//            )
    }
}