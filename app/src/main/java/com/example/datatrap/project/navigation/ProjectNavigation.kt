package com.example.datatrap.project.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.datatrap.about.navigation.AboutScreens
import com.example.datatrap.core.presentation.components.DrawerScreens
import com.example.datatrap.core.presentation.util.UiEvent
import com.example.datatrap.locality.navigation.LocalityScreens
import com.example.datatrap.login.navigation.LoginScreens
import com.example.datatrap.project.presentation.project_edit.ProjectScreen
import com.example.datatrap.project.presentation.project_edit.ProjectViewModel
import com.example.datatrap.project.presentation.project_list.ProjectListScreen
import com.example.datatrap.project.presentation.project_list.ProjectListScreenEvent
import com.example.datatrap.project.presentation.project_list.ProjectListViewModel
import com.example.datatrap.settings.navigation.SettingsScreens
import com.example.datatrap.specie.navigation.SpecieScreens
import com.example.datatrap.sync.navigation.SyncScreens
import kotlinx.coroutines.flow.collectLatest

fun NavGraphBuilder.projectNavigation(navController: NavHostController) {
    composable(
        route = ProjectScreens.ProjectListScreen.route
    ) {
        val viewModel: ProjectListViewModel = hiltViewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()

        ProjectListScreen(
            onEvent = { event ->
                when(event) {
                    is ProjectListScreenEvent.OnItemClick -> {
                        event.projectEntity
                        navController.navigate(route = LocalityScreens.LocalityListScreen.route)
                    }
                    is ProjectListScreenEvent.OnUpdateButtonClick -> {
                        navController.navigate(
                            route = ProjectScreens.ProjectScreen.passParams(
                                projectIdVal = event.projectEntity.projectId
                            )
                        )
                    }
                    is ProjectListScreenEvent.OnDrawerItemClick -> {
                        when(event.drawerScreen) {
                            DrawerScreens.SPECIES -> {
                                navController.navigate(route = SpecieScreens.SpecieListScreen.route)
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
                    ProjectListScreenEvent.OnAddButtonClick -> {
                        navController.navigate(route = ProjectScreens.ProjectScreen.route)
                    }
                    ProjectListScreenEvent.OnLogoutButtonClick -> {
                        navController.navigate(route = LoginScreens.LoginScreen.route)
                    }
                    else -> viewModel.onEvent(event)
                }
            },
            state = state
        )
    }

    composable(
        route = ProjectScreens.ProjectScreen.route,
        arguments = listOf(
            navArgument(ProjectScreens.ProjectScreen.projectIdKey) {
                type = NavType.StringType
                defaultValue = null
            },
        ),
    ) {
        val viewModel: ProjectViewModel = hiltViewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()

        LaunchedEffect(key1 = Unit) {
            viewModel.eventFlow.collectLatest { event ->
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