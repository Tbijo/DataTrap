package com.example.datatrap.session

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
import com.example.datatrap.occasion.navigateToOccasionListScreen
import com.example.datatrap.session.presentation.session_edit.SessionScreen
import com.example.datatrap.session.presentation.session_edit.SessionViewModel
import com.example.datatrap.session.presentation.session_list.SessionListScreen
import com.example.datatrap.session.presentation.session_list.SessionListScreenEvent
import com.example.datatrap.session.presentation.session_list.SessionListViewModel

private const val SESSION_LIST_SCREEN_ROUTE = "session_list_screen"
private const val SESSION_SCREEN_ROUTE = "session_screen"

fun NavController.navigateToSessionListScreen(screenNavArgs: ScreenNavArgs) {
    navigateToMainScreen(SESSION_LIST_SCREEN_ROUTE, screenNavArgs)
}
private fun NavController.navigateToSessionScreen(screenNavArgs: ScreenNavArgs) {
    navigateToMainScreen(SESSION_SCREEN_ROUTE, screenNavArgs)
}

fun NavGraphBuilder.sessionNavigation(navController: NavHostController) {

    composable(
        route = setMainRouteWithArgs(SESSION_LIST_SCREEN_ROUTE),
        arguments = getMainScreenArguments(),
    ) {
        val viewModel: SessionListViewModel = hiltViewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()

        val screenNavArgs = it.getMainScreenNavArgs() ?: ScreenNavArgs()

        SessionListScreen(
            onEvent = { event ->
                when(event) {
                    is SessionListScreenEvent.OnUpdateButtonClick -> {
                        navController.navigateToSessionScreen(
                            screenNavArgs = screenNavArgs.copy(
                                sessionId = event.sessionEntity.sessionId,
                            ),
                        )
                    }

                    is SessionListScreenEvent.OnItemClick -> {
                        // set num session in locality
                        viewModel.onEvent(SessionListScreenEvent.SetSesNumInLocality(event.sessionEntity.sessionId))
                        // navigate to occasion
                        navController.navigateToOccasionListScreen(
                            screenNavArgs = screenNavArgs.copy(
                                sessionId = event.sessionEntity.sessionId,
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
        route = setMainRouteWithArgs(SESSION_SCREEN_ROUTE),
        arguments = getMainScreenArguments(),
    ) {
        val viewModel: SessionViewModel = hiltViewModel()
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

        SessionScreen(
            onEvent = viewModel::onEvent,
            state = state,
        )
    }
}