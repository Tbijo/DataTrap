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
                    LocalityListScreenEvent.OnAddButtonClick -> TODO()
                    is LocalityListScreenEvent.OnItemClick -> TODO()
                    is LocalityListScreenEvent.OnUpdateButtonClick -> TODO()
                    is LocalityListScreenEvent.OnDeleteClick -> viewModel.onEvent(event)
                }
            },
            state = state,
        )
    }

    composable(
        route = LocalityScreens.LocalityScreen.route
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
            onEvent = {},
            state = state,
        )
    }

    composable(
        route = LocalityScreens.LocalityMapScreen.route
    ) {
        LocalityMapScreen()
    }
}