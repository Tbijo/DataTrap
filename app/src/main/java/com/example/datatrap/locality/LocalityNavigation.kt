package com.example.datatrap.locality

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.datatrap.core.presentation.util.UiEvent
import com.example.datatrap.locality.presentation.locality_add_edit.LocalityScreen
import com.example.datatrap.locality.presentation.locality_add_edit.LocalityViewModel
import com.example.datatrap.locality.presentation.locality_list.LocalityListScreen
import com.example.datatrap.locality.presentation.locality_list.LocalityListScreenEvent
import com.example.datatrap.locality.presentation.locality_list.LocalityListViewModel
import com.example.datatrap.locality.presentation.locality_map.LocalityMapScreen
import com.example.datatrap.locality.presentation.locality_map.LocalityMapViewModel
import com.example.datatrap.session.SessionListScreenRoute
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Serializable
data class LocalityListScreenRoute(
    val projectId: String? = null,
)
@Serializable
data class LocalityScreenRoute(
    val projectId: String?,
    val localityId: String? = null,
)
@Serializable
object LocalityMapScreenRoute

fun NavGraphBuilder.localityNavigation(navController: NavHostController) {

    composable<LocalityListScreenRoute> {
        val args = it.toRoute<LocalityListScreenRoute>()
        val viewModel: LocalityListViewModel = koinViewModel(
            parameters = {
                parametersOf(args.projectId)
            },
        )
        val state by viewModel.state.collectAsStateWithLifecycle()

        LocalityListScreen(
            onEvent = { event ->
                when(event) {
                    LocalityListScreenEvent.OnAddButtonClick ->
                        navController.navigate(
                            LocalityScreenRoute(
                                projectId = args.projectId,
                                localityId = null,
                            )
                        )

                    is LocalityListScreenEvent.OnUpdateButtonClick ->
                        navController.navigate(
                            LocalityScreenRoute(
                                projectId = args.projectId,
                                localityId = event.localityEntity.localityId,
                            )
                        )

                    is LocalityListScreenEvent.OnItemClick -> {
                        // set numLocal
                        viewModel.onEvent(LocalityListScreenEvent.SetNumLocalOfProject(event.localityId))
                        // To session screen needs projectId
                        navController.navigate(
                            SessionListScreenRoute(
                                projectId = args.projectId,
                                localityId = event.localityId,
                            )
                        )
                    }

                    LocalityListScreenEvent.OnMapButtonCLick ->
                        navController.navigate(LocalityMapScreenRoute)

                    else -> viewModel.onEvent(event)
                }
            },
            state = state,
        )
    }

    composable<LocalityScreenRoute> {
        val args = it.toRoute<LocalityScreenRoute>()
        val viewModel: LocalityViewModel = koinViewModel(
            parameters = {
                parametersOf(args.localityId)
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

        LocalityScreen(
            onEvent = viewModel::onEvent,
            state = state,
        )
    }

    composable<LocalityMapScreenRoute> {
        val viewModel: LocalityMapViewModel = koinViewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()

        LocalityMapScreen(
            state = state,
        )
    }
}