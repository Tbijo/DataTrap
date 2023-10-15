package com.example.datatrap.specie.navigation

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.datatrap.about.navigation.AboutScreens
import com.example.datatrap.core.presentation.components.DrawerScreens
import com.example.datatrap.project.navigation.ProjectScreens
import com.example.datatrap.settings.navigation.SettingsScreens
import com.example.datatrap.specie.presentation.specie_add_edit.SpecieScreen
import com.example.datatrap.specie.presentation.specie_add_edit.SpecieScreenEvent
import com.example.datatrap.specie.presentation.specie_add_edit.SpecieViewModel
import com.example.datatrap.specie.presentation.specie_detail.SpecieDetailScreen
import com.example.datatrap.specie.presentation.specie_detail.SpecieDetailViewModel
import com.example.datatrap.specie.presentation.specie_image.SpecieImageScreen
import com.example.datatrap.specie.presentation.specie_image.SpecieImageViewModel
import com.example.datatrap.specie.presentation.specie_list.SpecieListScreen
import com.example.datatrap.specie.presentation.specie_list.SpecieListScreenEvent
import com.example.datatrap.specie.presentation.specie_list.SpecieListViewModel
import com.example.datatrap.sync.navigation.SyncScreens

fun NavGraphBuilder.specieNavigation(navController: NavHostController) {

    composable(
        route = SpecieScreens.SpecieListScreen.route
    ) {
        val viewModel: SpecieListViewModel = hiltViewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()

        SpecieListScreen(
            onEvent = {
                when(it) {
                    SpecieListScreenEvent.OnAddButtonClick -> TODO()
                    is SpecieListScreenEvent.OnDrawerItemClick -> {
                        when(it.drawerScreen) {
                            DrawerScreens.PROJECTS -> {
                                navController.navigate(route = ProjectScreens.ProjectListScreen.route)
                            }
                            DrawerScreens.SETTINGS -> {
                                navController.navigate(route = SettingsScreens.SettingsListScreen.route)
                            }
                            DrawerScreens.ABOUT -> {
                                navController.navigate(route = AboutScreens.AboutScreen.route)
                            }
                            DrawerScreens.SYNCHRONIZE -> {
                                navController.navigate(route = SyncScreens.SynchronizeScreen.route)
                            }
                            else -> Unit
                        }
                    }
                    is SpecieListScreenEvent.OnItemClick -> TODO()
                    else -> viewModel.onEvent(it)
                }
            },
            state = state,
        )
    }

    composable(
        route = SpecieScreens.SpecieScreen.route
    ) {
        val viewModel: SpecieViewModel = hiltViewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()

        SpecieScreen(
            onEvent = { event ->
                when(event) {
                    SpecieScreenEvent.OnCameraClick -> TODO()
                    else -> viewModel.onEvent(event)
                }
            },
            state = state,
        )
    }

    composable(
        route = SpecieScreens.SpecieImageScreen.route
    ) {
        val viewModel: SpecieImageViewModel = hiltViewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()

        SpecieImageScreen(
            onEvent = viewModel::onEvent,
            state = state,
        )
    }

    composable(
        route = SpecieScreens.SpecieDetailScreen.route
    ) {
        val viewModel: SpecieDetailViewModel = hiltViewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()

        SpecieDetailScreen(
            onEvent = viewModel::onEvent,
            state = state,
        )
    }

}