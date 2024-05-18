package com.example.datatrap.project

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.datatrap.about.AboutScreenRoute
import com.example.datatrap.core.presentation.components.DrawerScreens
import com.example.datatrap.core.presentation.util.UiEvent
import com.example.datatrap.locality.LocalityListScreenRoute
import com.example.datatrap.project.presentation.project_edit.ProjectScreen
import com.example.datatrap.project.presentation.project_edit.ProjectViewModel
import com.example.datatrap.project.presentation.project_list.ProjectListScreen
import com.example.datatrap.project.presentation.project_list.ProjectListScreenEvent
import com.example.datatrap.project.presentation.project_list.ProjectListViewModel
import com.example.datatrap.settings.SettingsListScreenRoute
import com.example.datatrap.specie.SpecieListScreenRoute
import com.example.datatrap.sync.SyncScreenRoute
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Serializable
object ProjectListScreenRoute
@Serializable
data class ProjectScreenRoute(
    val projectId: String? = null,
)

fun NavGraphBuilder.projectNavigation(navController: NavHostController) {

    composable<ProjectListScreenRoute> {
        val viewModel: ProjectListViewModel = koinViewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()

        ProjectListScreen(
            onEvent = { event ->
                when(event) {
                    ProjectListScreenEvent.OnAddButtonClick -> {
                        navController.navigate(
                            ProjectScreenRoute()
                        )
                    }

                    is ProjectListScreenEvent.OnUpdateButtonClick -> {
                        navController.navigate(
                            ProjectScreenRoute(
                                projectId = event.projectEntity.projectId,
                            )
                        )
                    }

                    is ProjectListScreenEvent.OnItemClick -> {
                        navController.navigate(
                            LocalityListScreenRoute(
                                projectId = event.projectEntity.projectId,
                            )
                        )
                    }

                    is ProjectListScreenEvent.OnDrawerItemClick -> {
                        when(event.drawerScreen) {
                            DrawerScreens.SPECIES -> {
                                navController.navigate(SpecieListScreenRoute)
                            }
                            DrawerScreens.SETTINGS -> {
                                navController.navigate(SettingsListScreenRoute)
                            }
                            DrawerScreens.ABOUT -> {
                                navController.navigate(AboutScreenRoute)
                            }
                            DrawerScreens.SYNCHRONIZE -> {
                                navController.navigate(SyncScreenRoute)
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

    composable<ProjectScreenRoute> {
        val args = it.toRoute<ProjectScreenRoute>()
        val viewModel: ProjectViewModel = koinViewModel(
            parameters = {
                parametersOf(args.projectId)
            }
        )
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