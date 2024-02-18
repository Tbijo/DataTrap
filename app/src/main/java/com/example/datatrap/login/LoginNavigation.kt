package com.example.datatrap.login

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
import com.example.datatrap.project.navigateToProjectListScreen

const val LOGIN_SCREEN_ROUTE = "login_screen"

fun NavGraphBuilder.loginNavigation(navController: NavHostController) {
    composable(route = LOGIN_SCREEN_ROUTE) {
        val viewModel: LoginViewModel = hiltViewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()

        LaunchedEffect(Unit) {
            viewModel.eventFlow.collect { event ->
                when(event) {
                    UiEvent.NavigateNext -> {
                        navController.navigateToProjectListScreen()
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