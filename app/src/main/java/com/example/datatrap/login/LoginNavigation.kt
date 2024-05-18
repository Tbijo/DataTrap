package com.example.datatrap.login

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.datatrap.core.presentation.util.UiEvent
import com.example.datatrap.login.presentation.LoginScreen
import com.example.datatrap.login.presentation.LoginViewModel
import com.example.datatrap.project.ProjectListScreenRoute
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel

@Serializable
object LoginScreenRoute

fun NavGraphBuilder.loginNavigation(navController: NavHostController) {
    composable<LoginScreenRoute> {
        val viewModel: LoginViewModel = koinViewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()

        LaunchedEffect(Unit) {
            viewModel.eventFlow.collect { event ->
                when(event) {
                    UiEvent.NavigateNext -> {
                        navController.navigate(ProjectListScreenRoute)
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