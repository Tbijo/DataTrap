package com.example.datatrap.occasion.navigation

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
import com.example.datatrap.occasion.presentation.occasion_add_edit.OccasionScreen
import com.example.datatrap.occasion.presentation.occasion_add_edit.OccasionScreenEvent
import com.example.datatrap.occasion.presentation.occasion_add_edit.OccasionViewModel
import com.example.datatrap.occasion.presentation.occasion_detail.OccasionDetailScreen
import com.example.datatrap.occasion.presentation.occasion_detail.OccasionDetailViewModel
import com.example.datatrap.occasion.presentation.occasion_list.OccasionListScreen
import com.example.datatrap.occasion.presentation.occasion_list.OccasionListScreenEvent
import com.example.datatrap.occasion.presentation.occasion_list.OccasionListViewModel
import kotlinx.coroutines.flow.collectLatest

fun NavGraphBuilder.occasionNavigation(navController: NavHostController) {

    composable(
        route = OccasionScreens.OccasionListScreen.route,
        arguments = listOf(
            navArgument(OccasionScreens.OccasionListScreen.projectIdKey) {
                type = NavType.StringType
                defaultValue = null
            },
            navArgument(OccasionScreens.OccasionListScreen.sessionIdKey) {
                type = NavType.StringType
                defaultValue = null
            },
            navArgument(OccasionScreens.OccasionListScreen.localityIdKey) {
                type = NavType.StringType
                defaultValue = null
            },
        )
    ) {
        val viewModel: OccasionListViewModel = viewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()

        OccasionListScreen(
            onEvent = { event ->
                when(event) {
                    // sessionId, localityId,
                    OccasionListScreenEvent.OnAddButtonClick -> {
                        navController.navigate(
                            OccasionScreens.OccasionScreen.passParams(
                                projectIdVal = "",
                                localityIdVal = "",
                                sessionIdVal = "",
                                occasionIdVal = "",
                            )
                        )
                    }
                    // occasionId
                    is OccasionListScreenEvent.OnItemClick -> {
                        navController.navigate(
                            OccasionScreens.OccasionScreen.passParams(
                                projectIdVal = "",
                                localityIdVal = "",
                                sessionIdVal = "",
                                occasionIdVal = "",
                            )
                        )
                    }
                    // occasionId
                    is OccasionListScreenEvent.OnDetailButtonClick -> {
                        navController.navigate(
                            OccasionScreens.OccasionScreen.passParams(
                                projectIdVal = "",
                                localityIdVal = "",
                                sessionIdVal = "",
                                occasionIdVal = "",
                            )
                        )
                    }
                    // occasionId, localityId
                    is OccasionListScreenEvent.OnUpdateButtonClick -> {
                        navController.navigate(
                            OccasionScreens.OccasionScreen.passParams(
                                projectIdVal = "",
                                localityIdVal = "",
                                sessionIdVal = "",
                                occasionIdVal = "",
                            )
                        )
                    }
                    else -> viewModel.onEvent(event)
                }
            },
            state = state,
        )
    }

    composable(
        route = OccasionScreens.OccasionScreen.route
    ) {
        val viewModel: OccasionViewModel = viewModel()
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

        OccasionScreen(
            onEvent = { event ->
                when(event) {
                    OccasionScreenEvent.OnCameraClick -> {
                        // TODO Create Image, Return ID, Get the Image, Update its Parent ID when Occasion inserted

                        // "Occasion", args.occList.occasionId, ""
                        navController.navigate(
                            OccasionScreens.OccasionDetailScreen.passParams(
                                projectIdVal = "",
                                localityIdVal = "",
                                sessionIdVal = "",
                                occasionIdVal = "",
                            )
                        )
                    }
                    else -> Unit
                }
            },
            state = state,
        )
    }

    composable(
        route = OccasionScreens.OccasionDetailScreen.route
    ) {
        val viewModel: OccasionDetailViewModel = viewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()

        OccasionDetailScreen(
            onEvent = {

            },
            state = state,
        )
    }
}