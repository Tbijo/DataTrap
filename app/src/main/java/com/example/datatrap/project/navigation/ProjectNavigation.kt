package com.example.datatrap.project.navigation

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.datatrap.core.presentation.components.DrawerScreens
import com.example.datatrap.project.presentation.project_edit.ProjectScreen
import com.example.datatrap.project.presentation.project_list.ProjectListScreen
import com.example.datatrap.project.presentation.project_list.ProjectListScreenEvent
import com.example.datatrap.project.presentation.project_list.ProjectListViewModel

fun NavGraphBuilder.projectNavigation(navController: NavHostController) {
    composable(
        route = ProjectScreens.ProjectListScreen.route
    ) {
        val viewModel: ProjectListViewModel = viewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()

        ProjectListScreen(
            onEvent = { event ->
                when(event) {
                    is ProjectListScreenEvent.OnItemClick -> {
                        // prefViewModel.savePrjNamePref(project.projectName)
                        event.projectEntity
                        navController.navigate(route = Screen.LocalityListScreen.route)
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
                                navController.navigate(route = Screen.SpecieListScreen.route)
                            }
                            DrawerScreens.SETTINGS -> {
                                navController.navigate(route = Screen.SettingsListScreen.route)
                            }
                            DrawerScreens.ABOUT -> {
                                navController.navigate(route = Screen.AboutScreen.route)
                            }
                            DrawerScreens.SYNCHRONIZE -> {
                                navController.navigate(route = Screen.SynchronizeScreen.route)
                            }
                            else -> Unit
                        }
                    }
                    ProjectListScreenEvent.OnAddButtonClick -> {
                        navController.navigate(route = ProjectScreens.ProjectScreen.route)
                    }
                    ProjectListScreenEvent.OnLogoutButtonClick -> {
                        navController.navigate(route = Screen.LoginScreen.route)
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
        ProjectScreen(
            onEvent = {}
        )
    }
}