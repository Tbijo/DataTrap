package com.example.datatrap.login.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.datatrap.core.presentation.util.UiEvent
import com.example.datatrap.login.presentation.LoginScreen
import com.example.datatrap.login.presentation.LoginViewModel
import kotlinx.coroutines.flow.collectLatest

fun NavGraphBuilder.loginNavigation(navController: NavHostController) {
    composable(
        route = LoginScreens.LoginScreen.route
    ) {
        val viewModel: LoginViewModel = viewModel()
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

        LoginScreen(
            onEvent = viewModel::onEvent,
            state = state,
        )
    }
}