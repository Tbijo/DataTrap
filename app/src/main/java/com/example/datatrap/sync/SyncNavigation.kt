package com.example.datatrap.sync

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.datatrap.sync.presentation.SyncScreen
import com.example.datatrap.sync.presentation.SyncUiState
import kotlinx.serialization.Serializable

@Serializable
object SyncScreenRoute

fun NavGraphBuilder.syncNavigation(navController: NavHostController) {

    composable<SyncScreenRoute> {
        SyncScreen(
            onEvent = {},
            state = SyncUiState(),
        )
    }

}