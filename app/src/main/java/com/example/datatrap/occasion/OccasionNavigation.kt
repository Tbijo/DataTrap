package com.example.datatrap.occasion

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.datatrap.camera.EntityType
import com.example.datatrap.camera.navigateToCameraScreen
import com.example.datatrap.core.ScreenNavArgs
import com.example.datatrap.core.getMainScreenArguments
import com.example.datatrap.core.getMainScreenNavArgs
import com.example.datatrap.core.navigateToMainScreen
import com.example.datatrap.core.presentation.util.UiEvent
import com.example.datatrap.core.setMainRouteWithArgs
import com.example.datatrap.mouse.navigateToMouseListScreen
import com.example.datatrap.occasion.presentation.occasion_add_edit.OccasionScreen
import com.example.datatrap.occasion.presentation.occasion_add_edit.OccasionScreenEvent
import com.example.datatrap.occasion.presentation.occasion_add_edit.OccasionViewModel
import com.example.datatrap.occasion.presentation.occasion_detail.OccasionDetailScreen
import com.example.datatrap.occasion.presentation.occasion_detail.OccasionDetailViewModel
import com.example.datatrap.occasion.presentation.occasion_list.OccasionListScreen
import com.example.datatrap.occasion.presentation.occasion_list.OccasionListScreenEvent
import com.example.datatrap.occasion.presentation.occasion_list.OccasionListViewModel

private const val OCCASION_LIST_SCREEN_ROUTE = "occasion_list_screen"
private const val OCCASION_SCREEN_ROUTE = "occasion_screen"
private const val OCCASION_DETAIL_SCREEN_ROUTE = "occasion_detail_screen"

fun NavController.navigateToOccasionListScreen(screenNavArgs: ScreenNavArgs) {
    navigateToMainScreen(OCCASION_LIST_SCREEN_ROUTE, screenNavArgs)
}
private fun NavController.navigateToOccasionScreen(screenNavArgs: ScreenNavArgs) {
    navigateToMainScreen(OCCASION_SCREEN_ROUTE, screenNavArgs)
}
private fun NavController.navigateToOccasionDetailScreen(screenNavArgs: ScreenNavArgs) {
    navigateToMainScreen(OCCASION_DETAIL_SCREEN_ROUTE, screenNavArgs)
}

fun NavGraphBuilder.occasionNavigation(navController: NavHostController) {
    composable(
        route = setMainRouteWithArgs(OCCASION_LIST_SCREEN_ROUTE),
        arguments = getMainScreenArguments(),
    ) {
        val viewModel: OccasionListViewModel = hiltViewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()

        val screenNavArgs = it.getMainScreenNavArgs() ?: ScreenNavArgs()

        OccasionListScreen(
            onEvent = { event ->
                when(event) {
                    OccasionListScreenEvent.OnAddButtonClick -> {
                        navController.navigateToOccasionScreen(
                            screenNavArgs = screenNavArgs,
                        )
                    }

                    is OccasionListScreenEvent.OnUpdateButtonClick -> {
                        navController.navigateToOccasionScreen(
                            screenNavArgs = screenNavArgs.copy(
                                occasionId = event.occasionEntity.occasionId,
                            ),
                        )
                    }

                    is OccasionListScreenEvent.OnDetailButtonClick -> {
                        navController.navigateToOccasionDetailScreen(
                            screenNavArgs = screenNavArgs.copy(
                                occasionId = event.occasionEntity.occasionId,
                            ),
                        )
                    }

                    is OccasionListScreenEvent.OnItemClick -> {
                        navController.navigateToMouseListScreen(
                            screenNavArgs = screenNavArgs.copy(
                                occasionId = event.occasionEntity.occasionId,
                            ),
                        )
                    }

                    else -> viewModel.onEvent(event)
                }
            },
            state = state,
        )
    }

    composable(
        route = setMainRouteWithArgs(OCCASION_SCREEN_ROUTE),
        arguments = getMainScreenArguments(),
    ) {
        val viewModel: OccasionViewModel = hiltViewModel()
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

        OccasionScreen(
            onEvent = { event ->
                when(event) {
                    OccasionScreenEvent.OnCameraClick -> {
                        // TODO Create Image, Return ID, Get the Image, Update its Parent ID when Occasion inserted

                        // "Occasion", args.occList.occasionId, ""
                        navController.navigateToCameraScreen(  // TODO add occasionId
                            entityId = "",
                            entityType = EntityType.OCCASION,
                        )
                    }
                    else -> Unit
                }
            },
            state = state,
        )
    }

    composable(
        route = setMainRouteWithArgs(OCCASION_DETAIL_SCREEN_ROUTE),
        arguments = getMainScreenArguments(),
    ) {
        val viewModel: OccasionDetailViewModel = hiltViewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()

        OccasionDetailScreen(
            onEvent = viewModel::onEvent,
            state = state,
        )
    }
}