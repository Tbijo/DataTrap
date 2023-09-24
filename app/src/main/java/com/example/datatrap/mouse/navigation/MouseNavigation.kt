package com.example.datatrap.mouse.navigation

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
import com.example.datatrap.mouse.presentation.mouse_add_edit.MouseScreen
import com.example.datatrap.mouse.presentation.mouse_add_edit.MouseViewModel
import com.example.datatrap.mouse.presentation.mouse_add_multi.MouseMultiScreen
import com.example.datatrap.mouse.presentation.mouse_add_multi.MouseMultiViewModel
import com.example.datatrap.mouse.presentation.mouse_detail.MouseDetailScreen
import com.example.datatrap.mouse.presentation.mouse_detail.MouseDetailViewModel
import com.example.datatrap.mouse.presentation.mouse_list.MouseListScreen
import com.example.datatrap.mouse.presentation.mouse_list.MouseListScreenEvent
import com.example.datatrap.mouse.presentation.mouse_list.MouseListViewModel
import com.example.datatrap.mouse.presentation.mouse_recapture_list.RecaptureListScreen
import com.example.datatrap.mouse.presentation.mouse_recapture_list.RecaptureListViewModel
import com.example.datatrap.project.navigation.ProjectScreens
import kotlinx.coroutines.flow.collectLatest

fun NavGraphBuilder.mouseNavigation(navController: NavHostController) {

    composable(
        route = MouseScreens.MouseListScreen.route,
        arguments = listOf(
            navArgument(ProjectScreens.ProjectScreen.projectIdKey) {
                type = NavType.BoolType
                defaultValue = null
            },
        )
    ) {
        val viewModel: MouseListViewModel = viewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()

        MouseListScreen(
            onEvent = { event ->
                when(event) {
                    MouseListScreenEvent.OnAddButtonClick -> {
                        // TODO MouseScreen
                    }
                    is MouseListScreenEvent.OnItemClick -> {
                        // TODO MouseDetailScreen
                    }
                    is MouseListScreenEvent.OnRecaptureButtonClick -> {
                        // TODO RecaptureListScreen
                    }
                    is MouseListScreenEvent.OnUpdateButtonClick -> {
                        // TODO MouseScreen
                    }
                    MouseListScreenEvent.OnMultiButtonClick -> {
                        // TODO MouseMultiScreen
                    }
                    else -> viewModel.onEvent(event)
                }
            },
            state = state,
        )
    }

    composable(
        route = MouseScreens.MouseScreen.route,
    ) {
        val viewModel: MouseViewModel = viewModel()
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

        MouseScreen(
            onEvent = {},
            state = state,
        )
    }

    composable(
        route = MouseScreens.MouseDetailScreen.route,
    ) {
        val viewModel: MouseDetailViewModel = viewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()

        MouseDetailScreen(
            onEvent = {},
            state = state,
        )
    }

    composable(
        route = MouseScreens.MouseMultiScreen.route,
    ) {
        val viewModel: MouseMultiViewModel = viewModel()
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

        MouseMultiScreen(
            onEvent = {},
            state = state,
        )
    }

    composable(
        route = MouseScreens.MouseRecaptureListScreen.route,
    ) {
        val viewModel: RecaptureListViewModel = viewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()

        RecaptureListScreen(
            onEvent = {},
            state = state,
        )
    }

}