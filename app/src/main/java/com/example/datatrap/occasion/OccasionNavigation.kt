package com.example.datatrap.occasion

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.datatrap.camera.CameraScreenRoute
import com.example.datatrap.camera.getImageData
import com.example.datatrap.camera.util.EntityType
import com.example.datatrap.core.presentation.util.UiEvent
import com.example.datatrap.mouse.MouseListScreenRoute
import com.example.datatrap.occasion.presentation.occasion_add_edit.OccasionScreen
import com.example.datatrap.occasion.presentation.occasion_add_edit.OccasionScreenEvent
import com.example.datatrap.occasion.presentation.occasion_add_edit.OccasionViewModel
import com.example.datatrap.occasion.presentation.occasion_detail.OccasionDetailScreen
import com.example.datatrap.occasion.presentation.occasion_detail.OccasionDetailViewModel
import com.example.datatrap.occasion.presentation.occasion_list.OccasionListScreen
import com.example.datatrap.occasion.presentation.occasion_list.OccasionListScreenEvent
import com.example.datatrap.occasion.presentation.occasion_list.OccasionListViewModel
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Serializable
data class OccasionListScreenRoute(
    val projectId: String?,
    val localityId: String?,
    val sessionId: String?,
)
@Serializable
data class OccasionScreenRoute(
    val projectId: String?,
    val localityId: String?,
    val sessionId: String?,
    val occasionId: String?,
)
@Serializable
data class OccasionDetailScreenRoute(
    val projectId: String?,
    val localityId: String?,
    val sessionId: String?,
    val occasionId: String?,
)

fun NavGraphBuilder.occasionNavigation(navController: NavHostController) {
    composable<OccasionListScreenRoute> {
        val args = it.toRoute<OccasionListScreenRoute>()
        val viewModel: OccasionListViewModel = koinViewModel(
            parameters = {
                parametersOf(
                    args.sessionId,
                    args.localityId,
                )
            },
        )
        val state by viewModel.state.collectAsStateWithLifecycle()

        OccasionListScreen(
            onEvent = { event ->
                when(event) {
                    OccasionListScreenEvent.OnAddButtonClick -> {
                        navController.navigate(
                            OccasionScreenRoute(
                                projectId = args.projectId,
                                localityId = args.localityId,
                                sessionId = args.sessionId,
                                occasionId = null,
                            )
                        )
                    }

                    is OccasionListScreenEvent.OnUpdateButtonClick -> {
                        navController.navigate(
                            OccasionScreenRoute(
                                projectId = args.projectId,
                                localityId = args.localityId,
                                sessionId = args.sessionId,
                                occasionId = event.occasionEntity.occasionId,
                            )
                        )
                    }

                    is OccasionListScreenEvent.OnDetailButtonClick -> {
                        navController.navigate(
                            OccasionDetailScreenRoute(
                                projectId = args.projectId,
                                localityId = args.localityId,
                                sessionId = args.sessionId,
                                occasionId = event.occasionEntity.occasionId,
                            )
                        )
                    }

                    is OccasionListScreenEvent.OnItemClick -> {
                        navController.navigate(
                            MouseListScreenRoute(
                                projectId = args.projectId,
                                localityId = args.localityId,
                                sessionId = args.sessionId,
                                occasionId = event.occasionEntity.occasionId,
                            )
                        )
                    }

                    else -> viewModel.onEvent(event)
                }
            },
            state = state,
        )
    }

    composable<OccasionScreenRoute> {
        val args = it.toRoute<OccasionScreenRoute>()
        val viewModel: OccasionViewModel = koinViewModel(
            parameters = {
                parametersOf(
                    args.occasionId,
                    args.localityId,
                    args.sessionId,
                )
            },
        )
        val state by viewModel.state.collectAsStateWithLifecycle()
        val imageData = navController.getImageData()

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

        LaunchedEffect(key1 = imageData) {
            if (imageData?.change == true) {
                viewModel.onEvent(
                    OccasionScreenEvent.OnReceiveImageName(
                        imageName = imageData.name,
                        imageNote = imageData.note,
                    )
                )
            }
        }

        OccasionScreen(
            onEvent = { event ->
                when(event) {
                    is OccasionScreenEvent.OnCameraClick -> {
                        navController.navigate(
                            CameraScreenRoute(
                                entityType = EntityType.OCCASION,
                                entityId = event.occasionId,
                            )
                        )
                    }

                    is OccasionScreenEvent.OnLeave -> {
                        viewModel.onEvent(OccasionScreenEvent.OnLeave)
                        navController.navigateUp()
                    }

                    else -> viewModel.onEvent(event)
                }
            },
            state = state,
        )
    }

    composable<OccasionDetailScreenRoute> {
        val args = it.toRoute<OccasionDetailScreenRoute>()
        val viewModel: OccasionDetailViewModel = koinViewModel(
            parameters = {
                parametersOf(
                    args.occasionId,
                    args.localityId,
                )
            },
        )
        val state by viewModel.state.collectAsStateWithLifecycle()

        OccasionDetailScreen(
            onEvent = viewModel::onEvent,
            state = state,
        )
    }
}