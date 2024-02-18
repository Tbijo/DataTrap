package com.example.datatrap.project

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.datatrap.about.navigateToAboutScreen
import com.example.datatrap.core.ScreenNavArgs
import com.example.datatrap.core.getMainScreenArguments
import com.example.datatrap.core.navigateToMainScreen
import com.example.datatrap.core.presentation.components.DrawerScreens
import com.example.datatrap.core.presentation.util.UiEvent
import com.example.datatrap.core.setMainRouteWithArgs
import com.example.datatrap.locality.navigateToLocalityListScreen
import com.example.datatrap.project.presentation.project_edit.ProjectScreen
import com.example.datatrap.project.presentation.project_edit.ProjectViewModel
import com.example.datatrap.project.presentation.project_list.ProjectListScreen
import com.example.datatrap.project.presentation.project_list.ProjectListScreenEvent
import com.example.datatrap.project.presentation.project_list.ProjectListViewModel
import com.example.datatrap.settings.navigateToSettingsScreen
import com.example.datatrap.specie.navigateToSpecieListScreen
import com.example.datatrap.sync.navigateToSyncScreen

private const val PROJECT_LIST_SCREEN_ROUTE = "project_list_screen"
private const val PROJECT_SCREEN_ROUTE = "project_screen"

fun NavController.navigateToProjectListScreen() {
    navigate(PROJECT_LIST_SCREEN_ROUTE)
}

private fun NavController.navigateToProjectScreen(screenNavArgs: ScreenNavArgs) {
    navigateToMainScreen(PROJECT_SCREEN_ROUTE, screenNavArgs)
}

fun NavGraphBuilder.projectNavigation(navController: NavHostController) {

    composable(route = PROJECT_LIST_SCREEN_ROUTE) {
        val viewModel: ProjectListViewModel = hiltViewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()

        ProjectListScreen(
            onEvent = { event ->
                when(event) {
                    ProjectListScreenEvent.OnAddButtonClick -> {
                        navController.navigateToProjectScreen(
                            screenNavArgs = ScreenNavArgs(),
                        )
                    }

                    is ProjectListScreenEvent.OnUpdateButtonClick -> {
                        navController.navigateToProjectScreen(
                            screenNavArgs = ScreenNavArgs(
                                projectId = event.projectEntity.projectId,
                            ),
                        )
                    }

                    is ProjectListScreenEvent.OnItemClick -> {
                        navController.navigateToLocalityListScreen(
                            screenNavArgs = ScreenNavArgs(
                                projectId = event.projectEntity.projectId,
                            ),
                        )
                    }

                    is ProjectListScreenEvent.OnDrawerItemClick -> {
                        when(event.drawerScreen) {
                            DrawerScreens.SPECIES -> {
                                navController.navigateToSpecieListScreen()
                            }
                            DrawerScreens.SETTINGS -> {
                                navController.navigateToSettingsScreen()
                            }
                            DrawerScreens.ABOUT -> {
                                navController.navigateToAboutScreen()
                            }
                            DrawerScreens.SYNCHRONIZE -> {
                                navController.navigateToSyncScreen()
                            }
                            else -> Unit
                        }
                    }

                    ProjectListScreenEvent.OnLogoutButtonClick -> {
                        navController.navigateUp()
                    }

                    else -> viewModel.onEvent(event)
                }
            },
            state = state,
        )
    }

    composable(
        route = setMainRouteWithArgs(PROJECT_SCREEN_ROUTE),
        arguments = getMainScreenArguments(),
    ) {
        val viewModel: ProjectViewModel = hiltViewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()

        LaunchedEffect(Unit) {
            viewModel.eventFlow.collect { event ->
                when(event) {
                    UiEvent.NavigateBack -> {
                        navController.navigateUp()
                    }
                    else -> Unit
                }
            }
        }

        ProjectScreen(
            onEvent = viewModel::onEvent,
            state = state,
        )
    }
}