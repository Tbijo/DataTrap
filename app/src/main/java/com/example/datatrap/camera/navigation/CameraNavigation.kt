package com.example.datatrap.camera.navigation

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.datatrap.camera.presentation.CameraScreen
import com.example.datatrap.camera.presentation.CameraViewModel

fun NavGraphBuilder.cameraNavigation(navController: NavHostController) {

    composable(
        route = CameraScreens.CameraScreen.route
    ) {
        val viewModel: CameraViewModel = viewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()

        CameraScreen(
            onEvent = {},
            state = state,
        )
    }

}