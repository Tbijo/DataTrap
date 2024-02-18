package com.example.datatrap.locality

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.datatrap.core.ScreenNavArgs
import com.example.datatrap.core.getMainScreenArguments
import com.example.datatrap.core.getMainScreenNavArgs
import com.example.datatrap.core.navigateToMainScreen
import com.example.datatrap.core.presentation.util.UiEvent
import com.example.datatrap.core.setMainRouteWithArgs
import com.example.datatrap.locality.presentation.locality_add_edit.LocalityScreen
import com.example.datatrap.locality.presentation.locality_add_edit.LocalityViewModel
import com.example.datatrap.locality.presentation.locality_list.LocalityListScreen
import com.example.datatrap.locality.presentation.locality_list.LocalityListScreenEvent
import com.example.datatrap.locality.presentation.locality_list.LocalityListViewModel
import com.example.datatrap.locality.presentation.locality_map.LocalityMapScreen
import com.example.datatrap.locality.presentation.locality_map.LocalityMapViewModel
import com.example.datatrap.session.navigateToSessionListScreen

private const val LOCALITY_LIST_SCREEN_ROUTE = "locality_list_screen"
private const val LOCALITY_SCREEN_ROUTE = "locality_screen"
private const val LOCALITY_MAP_SCREEN_ROUTE = "locality_map_screen"

fun NavController.navigateToLocalityListScreen(screenNavArgs: ScreenNavArgs) {
    navigateToMainScreen(LOCALITY_LIST_SCREEN_ROUTE, screenNavArgs)
}
private fun NavController.navigateToLocalityScreen(screenNavArgs: ScreenNavArgs) {
    navigateToMainScreen(LOCALITY_SCREEN_ROUTE, screenNavArgs)
}
private fun NavController.navigateToLocalityMapScreen() {
    navigate(LOCALITY_MAP_SCREEN_ROUTE)
}

fun NavGraphBuilder.localityNavigation(navController: NavHostController) {

    composable(
        route = setMainRouteWithArgs(LOCALITY_LIST_SCREEN_ROUTE),
        arguments = getMainScreenArguments(),
    ) {
        val viewModel: LocalityListViewModel = hiltViewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()

        val screenNavArgs = it.getMainScreenNavArgs() ?: ScreenNavArgs()

        LocalityListScreen(
            onEvent = { event ->
                when(event) {
                    LocalityListScreenEvent.OnAddButtonClick -> navController.navigateToLocalityScreen(
                        screenNavArgs = screenNavArgs
                    )

                    is LocalityListScreenEvent.OnUpdateButtonClick -> navController.navigateToLocalityScreen(
                        screenNavArgs = screenNavArgs.copy(
                            localityId = event.localityEntity.localityId,
                        ),
                    )

                    is LocalityListScreenEvent.OnItemClick -> {
                        // set numLocal
                        viewModel.onEvent(LocalityListScreenEvent.SetNumLocalOfProject(event.localityId))
                        // To session screen needs projectId
                        navController.navigateToSessionListScreen(
                            screenNavArgs = screenNavArgs.copy(
                                localityId = event.localityId,
                            ),
                        )
                    }

                    LocalityListScreenEvent.OnMapButtonCLick -> navController.navigateToLocalityMapScreen()

                    else -> viewModel.onEvent(event)
                }
            },
            state = state,
        )
    }

    composable(
        route = setMainRouteWithArgs(LOCALITY_SCREEN_ROUTE),
        arguments = getMainScreenArguments(),
    ) {
        val viewModel: LocalityViewModel = hiltViewModel()
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

        LocalityScreen(
            onEvent = viewModel::onEvent,
            state = state,
        )
    }

    composable(route = LOCALITY_MAP_SCREEN_ROUTE) {
        val viewModel: LocalityMapViewModel = hiltViewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()

        LocalityMapScreen(
            state = state,
        )
    }
}