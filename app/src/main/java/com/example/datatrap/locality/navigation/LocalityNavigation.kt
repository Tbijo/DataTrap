package com.example.datatrap.locality.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.datatrap.core.presentation.util.UiEvent
import com.example.datatrap.locality.presentation.locality_add_edit.LocalityScreen
import com.example.datatrap.locality.presentation.locality_add_edit.LocalityViewModel
import com.example.datatrap.locality.presentation.locality_list.LocalityListScreen
import com.example.datatrap.locality.presentation.locality_list.LocalityListScreenEvent
import com.example.datatrap.locality.presentation.locality_list.LocalityListViewModel
import com.example.datatrap.locality.presentation.locality_map.LocalityMapScreen
import com.example.datatrap.locality.presentation.locality_map.LocalityMapViewModel
import com.example.datatrap.session.navigation.SessionScreens
import kotlinx.coroutines.flow.collectLatest

fun NavGraphBuilder.localityNavigation(navController: NavHostController) {

    composable(
        route = LocalityScreens.LocalityListScreen.route,
        arguments = listOf(
            navArgument(LocalityScreens.LocalityListScreen.projectIdKey) {
                type = NavType.StringType
                defaultValue = null
            },
        ),
    ) {
        val viewModel: LocalityListViewModel = viewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()

        LocalityListScreen(
            onEvent = { event ->
                when(event) {
                    LocalityListScreenEvent.OnAddButtonClick ->
                        navController.navigate(
                            LocalityScreens.LocalityScreen.passParams(
                                localityIdVal = null
                            )
                        )
                    is LocalityListScreenEvent.OnItemClick -> {
                        // To session screen needs projectId
                        navController.navigate(
                            SessionScreens.SessionListScreen.passParams(
                                projectIdVal = "",
                                localityIdVal = event.localityEntity.localityId,
                            )
                        )
                    }
                    is LocalityListScreenEvent.OnUpdateButtonClick -> {
                        // Update Loc locId needed
                        navController.navigate(
                            LocalityScreens.LocalityScreen.passParams(
                                localityIdVal = event.localityEntity.localityId
                            )
                        )
                    }
                    LocalityListScreenEvent.OnMapButtonCLick -> {
                        // MAP
                        navController.navigate(
                            LocalityScreens.LocalityMapScreen.route
                        )
                    }
                    else -> viewModel.onEvent(event)
                }
            },
            state = state,
        )
    }

    composable(
        route = LocalityScreens.LocalityScreen.route,
        arguments = listOf(
            navArgument(LocalityScreens.LocalityScreen.localityIdKey) {
                type = NavType.StringType
                defaultValue = null
            },
        )
    ) {
        val viewModel: LocalityViewModel = viewModel()
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

        LocalityScreen(
            onEvent = viewModel::onEvent,
            state = state,
        )
    }

    composable(
        route = LocalityScreens.LocalityMapScreen.route
    ) {
        val viewModel: LocalityMapViewModel = viewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()

        LocalityMapScreen(
            state = state,
        )
    }
}