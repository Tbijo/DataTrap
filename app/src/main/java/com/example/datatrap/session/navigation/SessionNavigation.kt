package com.example.datatrap.session.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.datatrap.core.presentation.util.UiEvent
import com.example.datatrap.occasion.navigation.OccasionScreens
import com.example.datatrap.session.presentation.session_edit.SessionScreen
import com.example.datatrap.session.presentation.session_edit.SessionViewModel
import com.example.datatrap.session.presentation.session_list.SessionListScreen
import com.example.datatrap.session.presentation.session_list.SessionListScreenEvent
import com.example.datatrap.session.presentation.session_list.SessionListViewModel
import kotlinx.coroutines.flow.collectLatest

fun NavGraphBuilder.sessionNavigation(navController: NavHostController) {
    composable(
        route = SessionScreens.SessionListScreen.route,
        arguments = listOf(
            navArgument(SessionScreens.SessionListScreen.projectIdKey) {
                type = NavType.StringType
                defaultValue = null
            },
            navArgument(SessionScreens.SessionListScreen.localityIdKey) {
                type = NavType.StringType
                defaultValue = null
            },
        ),
    ) {
        val viewModel: SessionListViewModel = hiltViewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()

        SessionListScreen(
            onEvent = { event ->
                when(event) {
                    is SessionListScreenEvent.OnItemClick -> {
                        navController.navigate(OccasionScreens.OccasionListScreen.passParams(
                            projectIdVal = event.sessionEntity.projectID,
                            localityIdVal = event.localityId,
                            sessionIdVal = event.sessionEntity.sessionId,
                        ))
                    }
                    is SessionListScreenEvent.OnUpdateButtonClick -> {
                        navController.navigate(SessionScreens.SessionScreen.passParams(
                            sessionIdVal = event.sessionEntity.sessionId,
                        ))
                    }
                    else -> viewModel.onEvent(event)
                }
            },
            state = state,
        )
    }

    composable(
        route = SessionScreens.SessionScreen.route,
        arguments = listOf(
            navArgument(SessionScreens.SessionScreen.sessionIdKey) {
                type = NavType.StringType
                defaultValue = null
            },
        ),
    ) {
        val viewModel: SessionViewModel = hiltViewModel()
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

        SessionScreen(
            onEvent = viewModel::onEvent,
            state = state,
        )
    }
}