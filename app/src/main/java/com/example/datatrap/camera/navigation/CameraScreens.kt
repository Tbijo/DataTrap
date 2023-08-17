package com.example.datatrap.camera.navigation

sealed class CameraScreens(val route: String) {
    object CameraScreen: CameraScreens("camera_screen")
}