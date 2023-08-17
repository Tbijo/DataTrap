package com.example.datatrap.sync.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.datatrap.sync.presentation.SyncScreen

fun NavGraphBuilder.syncNavigation(navController: NavHostController) {

    composable(
        route = SyncScreens.SynchronizeScreen.route
    ) {
        SyncScreen(
            onEvent = {}
        )
    }

}