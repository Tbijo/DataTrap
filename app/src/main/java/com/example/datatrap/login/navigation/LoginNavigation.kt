package com.example.datatrap.login.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.datatrap.core.presentation.util.UiEvent
import com.example.datatrap.login.presentation.LoginScreen
import com.example.datatrap.login.presentation.LoginViewModel
import com.example.datatrap.project.navigation.ProjectScreens

fun NavGraphBuilder.loginNavigation(navController: NavHostController) {
    composable(
        route = LoginScreens.LoginScreen.route
    ) {
        val viewModel: LoginViewModel = hiltViewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()

        LaunchedEffect(Unit) {
            viewModel.eventFlow.collect { event ->
                when(event) {
                    UiEvent.NavigateNext -> {
                        navController.navigate(ProjectScreens.ProjectListScreen.route)
                    }
                    else -> Unit
                }
            }
        }

        LoginScreen(
            onEvent = viewModel::onEvent,
            state = state,
        )
    }
}