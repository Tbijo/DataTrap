package com.example.datatrap.about

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.datatrap.about.presentation.AboutScreen
import com.example.datatrap.about.presentation.AboutUiState
import com.example.datatrap.core.presentation.components.DrawerScreens
import com.example.datatrap.project.ProjectListScreenRoute
import com.example.datatrap.settings.SettingsListScreenRoute
import com.example.datatrap.specie.SpecieListScreenRoute
import com.example.datatrap.sync.SyncScreenRoute
import kotlinx.serialization.Serializable

@Serializable
object AboutScreenRoute

fun NavGraphBuilder.aboutNavigation(navController: NavHostController) {
    composable<AboutScreenRoute> {
        AboutScreen(
            onEvent = { event ->
                when(event) {
                    DrawerScreens.SPECIES -> {
                        navController.navigate(SpecieListScreenRoute)
                    }
                    DrawerScreens.SETTINGS -> {
                        navController.navigate(SettingsListScreenRoute)
                    }
                    DrawerScreens.PROJECTS -> {
                        navController.navigate(ProjectListScreenRoute)
                    }
                    DrawerScreens.SYNCHRONIZE -> {
                        navController.navigate(SyncScreenRoute)
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