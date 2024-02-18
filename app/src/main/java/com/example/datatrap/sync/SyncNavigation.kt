package com.example.datatrap.sync

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.datatrap.sync.presentation.SyncScreen
import com.example.datatrap.sync.presentation.SyncUiState

private const val SYNC_SCREEN_ROUTE = "synchronize_screen"

fun NavController.navigateToSyncScreen() = navigate(SYNC_SCREEN_ROUTE)

fun NavGraphBuilder.syncNavigation(navController: NavHostController) {

    composable(SYNC_SCREEN_ROUTE) {
        SyncScreen(
            onEvent = {},
            state = SyncUiState(),
        )
    }

}