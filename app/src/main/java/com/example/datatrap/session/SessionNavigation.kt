package com.example.datatrap.session

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.datatrap.core.presentation.util.UiEvent
import com.example.datatrap.occasion.OccasionListScreenRoute
import com.example.datatrap.session.presentation.session_edit.SessionScreen
import com.example.datatrap.session.presentation.session_edit.SessionViewModel
import com.example.datatrap.session.presentation.session_list.SessionListScreen
import com.example.datatrap.session.presentation.session_list.SessionListScreenEvent
import com.example.datatrap.session.presentation.session_list.SessionListViewModel
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Serializable
data class SessionListScreenRoute(
    val projectId: String?,
    val localityId: String?,
)
@Serializable
data class SessionScreenRoute(
    val projectId: String?,
    val sessionId: String?,
)

fun NavGraphBuilder.sessionNavigation(navController: NavHostController) {

    composable<SessionListScreenRoute> {
        val args = it.toRoute<SessionListScreenRoute>()
        val viewModel: SessionListViewModel = koinViewModel(
            parameters = {
                parametersOf(
                    args.localityId,
                    args.projectId,
                )
            },
        )
        val state by viewModel.state.collectAsStateWithLifecycle()

        SessionListScreen(
            onEvent = { event ->
                when(event) {
                    is SessionListScreenEvent.OnUpdateButtonClick -> {
                        navController.navigate(
                            SessionScreenRoute(
                                projectId = args.projectId,
                                sessionId = event.sessionEntity.sessionId,
                            )
                        )
                    }

                    is SessionListScreenEvent.OnItemClick -> {
                        // set num session in locality
                        viewModel.onEvent(SessionListScreenEvent.SetSesNumInLocality(event.sessionEntity.sessionId))
                        // navigate to occasion
                        navController.navigate(
                            OccasionListScreenRoute(
                                projectId = args.projectId,
                                localityId = args.localityId,
                                sessionId = event.sessionEntity.sessionId,
                            )
                        )
                    }

                    else -> viewModel.onEvent(event)
                }
            },
            state = state,
        )
    }

    composable<SessionScreenRoute> {
        val args = it.toRoute<SessionScreenRoute>()
        val viewModel: SessionViewModel = koinViewModel(
            parameters = {
                parametersOf(
                    args.sessionId,
                    args.projectId,
                )
            },
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

        SessionScreen(
            onEvent = viewModel::onEvent,
            state = state,
        )
    }
}