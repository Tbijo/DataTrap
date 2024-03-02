package com.example.datatrap.mouse

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.datatrap.camera.clearImageChange
import com.example.datatrap.camera.getImageChange
import com.example.datatrap.camera.getImageName
import com.example.datatrap.camera.getImageNote
import com.example.datatrap.camera.navigateToCameraScreen
import com.example.datatrap.camera.util.EntityType
import com.example.datatrap.core.ScreenNavArgs
import com.example.datatrap.core.getMainScreenArguments
import com.example.datatrap.core.getMainScreenNavArgs
import com.example.datatrap.core.navigateToMainScreen
import com.example.datatrap.core.presentation.util.UiEvent
import com.example.datatrap.core.setMainRouteWithArgs
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

private const val MOUSE_LIST_SCREEN_ROUTE = "mouse_list_screen"
private const val MOUSE_SCREEN_ROUTE = "mouse_screen"
private const val MOUSE_MULTI_SCREEN_ROUTE = "mouse_multi_screen"
private const val MOUSE_DETAIL_SCREEN_ROUTE = "mouse_detail_screen"
private const val MOUSE_RECAP_LIST_SCREEN_ROUTE = "mouse_recap_list_screen"

fun NavController.navigateToMouseListScreen(screenNavArgs: ScreenNavArgs) {
    navigateToMainScreen(MOUSE_LIST_SCREEN_ROUTE, screenNavArgs)
}
private fun NavController.navigateToMouseScreen(screenNavArgs: ScreenNavArgs) {
    navigateToMainScreen(MOUSE_SCREEN_ROUTE, screenNavArgs)
}
private fun NavController.navigateToMouseMultiScreen(screenNavArgs: ScreenNavArgs) {
    navigateToMainScreen(MOUSE_MULTI_SCREEN_ROUTE, screenNavArgs)
}
private fun NavController.navigateToMouseDetailScreen(screenNavArgs: ScreenNavArgs) {
    navigateToMainScreen(MOUSE_DETAIL_SCREEN_ROUTE, screenNavArgs)
}
private fun NavController.navigateToMouseRecapScreen(screenNavArgs: ScreenNavArgs) {
    navigateToMainScreen(MOUSE_RECAP_LIST_SCREEN_ROUTE, screenNavArgs)
}

// TODO Create composables() for MouseScreen with separate viewModels for Insert Update and Recapture

fun NavGraphBuilder.mouseNavigation(navController: NavHostController) {

    composable(
        route = setMainRouteWithArgs(MOUSE_LIST_SCREEN_ROUTE),
        arguments = getMainScreenArguments(),
    ) {
        val viewModel: MouseListViewModel = hiltViewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()

        val screenNavArgs = it.getMainScreenNavArgs() ?: ScreenNavArgs()

        MouseListScreen(
            onEvent = { event ->
                when(event) {
                    MouseListScreenEvent.OnAddButtonClick -> {
                        navController.navigateToMouseScreen(
                            screenNavArgs = screenNavArgs,
                        )
                    }

                    is MouseListScreenEvent.OnUpdateButtonClick -> {
                        navController.navigateToMouseScreen(
                            screenNavArgs = screenNavArgs.copy(
                                mouseId = event.mouseId,
                            ),
                        )
                    }

                    is MouseListScreenEvent.OnItemClick -> {
                        navController.navigateToMouseDetailScreen(
                            screenNavArgs = screenNavArgs.copy(
                                mouseId = event.mouseId,
                            ),
                        )
                    }

                    is MouseListScreenEvent.OnRecaptureButtonClick -> {
                        navController.navigateToMouseRecapScreen(
                            screenNavArgs = screenNavArgs,
                        )
                    }

                    MouseListScreenEvent.OnMultiButtonClick -> {
                        navController.navigateToMouseMultiScreen(
                            screenNavArgs = screenNavArgs,
                        )
                    }

                    else -> viewModel.onEvent(event)
                }
            },
            state = state,
        )
    }

    composable(
        route = setMainRouteWithArgs(MOUSE_SCREEN_ROUTE),
        arguments = getMainScreenArguments(),
    ) {
        val viewModel: MouseViewModel = hiltViewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()
        val imageName = navController.getImageName()
        val imageNote = navController.getImageNote()
        val makeChange = navController.getImageChange()

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

        LaunchedEffect(key1 = imageName, key2 = imageNote, key3 = makeChange) {
            if (makeChange == true) {
                viewModel.onEvent(
                    MouseScreenEvent.OnReceiveImageName(
                        imageName = imageName,
                        imageNote = imageNote,
                    )
                )
                // clear for config change
                navController.clearImageChange()
            }
        }

        MouseScreen(
            onEvent = { event ->
                when(event) {
                    is MouseScreenEvent.OnCameraClick -> {
                        navController.navigateToCameraScreen(
                            entityType = EntityType.MOUSE,
                            imageId = event.imageId,
                        )
                    }

                    else -> viewModel.onEvent(event)
                }
            },
            state = state,
        )
    }

    composable(
        route = setMainRouteWithArgs(MOUSE_DETAIL_SCREEN_ROUTE),
        arguments = getMainScreenArguments(),
    ) {
        val viewModel: MouseDetailViewModel = hiltViewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()

        MouseDetailScreen(
            onEvent = viewModel::onEvent,
            state = state,
        )
    }

    composable(
        route = setMainRouteWithArgs(MOUSE_MULTI_SCREEN_ROUTE),
        arguments = getMainScreenArguments(),
    ) {
        val viewModel: MouseMultiViewModel = hiltViewModel()
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

    composable(
        route = setMainRouteWithArgs(MOUSE_RECAP_LIST_SCREEN_ROUTE),
        arguments = getMainScreenArguments(),
    ) {
        val viewModel: RecaptureListViewModel = hiltViewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()

        val screenNavArgs = it.getMainScreenNavArgs() ?: ScreenNavArgs()

        RecaptureListScreen(
            onEvent = { event ->
                when(event) {
                    is RecaptureListScreenEvent.OnItemClick -> {
                        navController.navigateToMouseScreen(
                            screenNavArgs = screenNavArgs.copy(
                                isRecapture = true,
                                mouseId = event.mouse.mouseId,
                                primeMouseId = event.mouse.primeMouseID,
                            ),
                        )
                    }

                    else -> viewModel.onEvent(event)
                }
            },
            state = state,
        )
    }

}