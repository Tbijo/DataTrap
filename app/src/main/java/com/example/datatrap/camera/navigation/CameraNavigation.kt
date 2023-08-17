package com.example.datatrap.camera.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.datatrap.camera.presentation.CameraScreen

fun NavGraphBuilder.cameraNavigation(navController: NavHostController) {

    composable(
        route = CameraScreens.CameraScreen.route
    ) {
        CameraScreen(
            onEvent = {}
        )
    }

}