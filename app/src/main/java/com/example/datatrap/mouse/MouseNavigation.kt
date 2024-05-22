package com.example.datatrap.mouse

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
import com.example.datatrap.mouse.presentation.mouse_add_edit.MouseScreen
import com.example.datatrap.mouse.presentation.mouse_add_edit.MouseScreenEvent
import com.example.datatrap.mouse.presentation.mouse_add_edit.MouseViewModel
import com.example.datatrap.mouse.presentation.mouse_add_multi.MouseMultiScreen
import com.example.datatrap.mouse.presentation.mouse_add_multi.MouseMultiViewModel
import com.example.datatrap.mouse.presentation.mouse_detail.MouseDetailScreen
import com.example.datatrap.mouse.presentation.mouse_detail.MouseDetailViewModel
import com.example.datatrap.mouse.presentation.mouse_list.MouseListScreen
import com.example.datatrap.mouse.presentation.mouse_list.MouseListScreenEvent
import com.example.datatrap.mouse.presentation.mouse_list.MouseListViewModel
import com.example.datatrap.mouse.presentation.mouse_recapture_list.RecaptureListScreen
import com.example.datatrap.mouse.presentation.mouse_recapture_list.RecaptureListScreenEvent
import com.example.datatrap.mouse.presentation.mouse_recapture_list.RecaptureListViewModel
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Serializable
data class MouseListScreenRoute(
    val projectId: String?,
    val localityId: String?,
    val sessionId: String?,
    val occasionId: String?,
)
@Serializable
data class MouseScreenRoute(
    val projectId: String?,
    val localityId: String?,
    val sessionId: String?,
    val occasionId: String?,
    val mouseId: String?,
    val primeMouseId: String?,
    val isRecapture: Boolean,
)
@Serializable
data class MouseMultiScreenRoute(
    val projectId: String?,
    val localityId: String?,
    val sessionId: String?,
    val occasionId: String?,
    val mouseId: String?,
    val primeMouseId: String?,
    val isRecapture: Boolean,
)
@Serializable
data class MouseDetailScreenRoute(
    val projectId: String?,
    val localityId: String?,
    val sessionId: String?,
    val occasionId: String?,
    val mouseId: String?,
    val primeMouseId: String?,
    val isRecapture: Boolean,
)
@Serializable
data class MouseRecapListScreenRoute(
    val projectId: String?,
    val localityId: String?,
    val sessionId: String?,
    val occasionId: String?,
    val mouseId: String?,
    val primeMouseId: String?,
    val isRecapture: Boolean,
)

// TODO Create composables() for MouseScreen with separate viewModels for Insert Update and Recapture
fun NavGraphBuilder.mouseNavigation(navController: NavHostController) {

    composable<MouseListScreenRoute> {
        val args = it.toRoute<MouseListScreenRoute>()
        val viewModel: MouseListViewModel = koinViewModel(
            parameters = {
                parametersOf(
                    args.projectId,
                    args.localityId,
                    args.sessionId,
                    args.occasionId,
                )
            }
        )
        val state by viewModel.state.collectAsStateWithLifecycle()

        MouseListScreen(
            onEvent = { event ->
                when(event) {
                    MouseListScreenEvent.OnAddButtonClick -> {
                        navController.navigate(
                            MouseScreenRoute(
                                projectId = args.projectId,
                                localityId = args.localityId,
                                sessionId = args.sessionId,
                                occasionId = args.occasionId,
                                mouseId = null,
                                primeMouseId = null,
                                isRecapture = false,
                            )
                        )
                    }

                    is MouseListScreenEvent.OnUpdateButtonClick -> {
                        navController.navigate(
                            MouseScreenRoute(
                                projectId = args.projectId,
                                localityId = args.localityId,
                                sessionId = args.sessionId,
                                occasionId = args.occasionId,
                                mouseId = event.mouseId,
                                primeMouseId = null,
                                isRecapture = false,
                            )
                        )
                    }

                    is MouseListScreenEvent.OnItemClick -> {
                        navController.navigate(
                            MouseDetailScreenRoute(
                                projectId = args.projectId,
                                localityId = args.localityId,
                                sessionId = args.sessionId,
                                occasionId = args.occasionId,
                                mouseId = event.mouseId,
                                primeMouseId = null,
                                isRecapture = false,
                            )
                        )
                    }

                    is MouseListScreenEvent.OnRecaptureButtonClick -> {
                        navController.navigate(
                            MouseRecapListScreenRoute(
                                projectId = args.projectId,
                                localityId = args.localityId,
                                sessionId = args.sessionId,
                                occasionId = args.occasionId,
                                mouseId = null,
                                primeMouseId = null,
                                isRecapture = false,
                            )
                        )
                    }

                    MouseListScreenEvent.OnMultiButtonClick -> {
                        navController.navigate(
                            MouseMultiScreenRoute(
                                projectId = args.projectId,
                                localityId = args.localityId,
                                sessionId = args.sessionId,
                                occasionId = args.occasionId,
                                mouseId = null,
                                primeMouseId = null,
                                isRecapture = false,
                            )
                        )
                    }

                    else -> viewModel.onEvent(event)
                }
            },
            state = state,
        )
    }

    composable<MouseScreenRoute> {
        val args = it.toRoute<MouseScreenRoute>()
        val viewModel: MouseViewModel = koinViewModel(
            parameters = {
                parametersOf(
                    args.localityId,
                    args.occasionId,
                    args.mouseId,
                    args.isRecapture,
                )
            }
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
                    MouseScreenEvent.OnReceiveImageName(
                        imageName = imageData.name,
                        imageNote = imageData.note,
                    )
                )
            }
        }

        MouseScreen(
            onEvent = { event ->
                when(event) {
                    is MouseScreenEvent.OnCameraClick -> {
                        navController.navigate(
                            CameraScreenRoute(
                                entityType = EntityType.MOUSE,
                                entityId = event.mouseId,
                            )
                        )
                    }
                    is MouseScreenEvent.OnLeave -> {
                        viewModel.onEvent(MouseScreenEvent.OnLeave)
                        navController.navigateUp()
                    }

                    else -> viewModel.onEvent(event)
                }
            },
            state = state,
        )
    }

    composable<MouseDetailScreenRoute> {
        val args = it.toRoute<MouseDetailScreenRoute>()
        val viewModel: MouseDetailViewModel = koinViewModel(
            parameters = {
                parametersOf(args.mouseId)
            }
        )
        val state by viewModel.state.collectAsStateWithLifecycle()

        MouseDetailScreen(
            onEvent = viewModel::onEvent,
            state = state,
        )
    }

    composable<MouseMultiScreenRoute> {
        val args = it.toRoute<MouseMultiScreenRoute>()
        val viewModel: MouseMultiViewModel = koinViewModel(
            parameters = {
                parametersOf(
                    args.localityId,
                    args.occasionId,
                )
            }
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

        MouseMultiScreen(
            onEvent = viewModel::onEvent,
            state = state,
        )
    }

    composable<MouseRecapListScreenRoute> {
        val args = it.toRoute<MouseRecapListScreenRoute>()
        val viewModel: RecaptureListViewModel = koinViewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()

        RecaptureListScreen(
            onEvent = { event ->
                when(event) {
                    is RecaptureListScreenEvent.OnItemClick -> {
                        navController.navigate(
                            MouseScreenRoute(
                                projectId = args.projectId,
                                localityId = args.localityId,
                                sessionId = args.sessionId,
                                occasionId = args.occasionId,
                                mouseId = event.mouse.mouseId,
                                primeMouseId = event.mouse.primeMouseID,
                                isRecapture = true,
                            )
                        )
                    }

                    else -> viewModel.onEvent(event)
                }
            },
            state = state,
        )
    }

}