package com.example.datatrap.camera.navigation

sealed class CameraScreens(val route: String) {
    object CameraScreen: CameraScreens("camera_screen/{entityIdKey}/{entityTypeKey}") {
        const val entityIdKey = "entityIdKey"
        const val entityTypeKey = "entityTypeKey"

        fun passParams(entityIdVal: String, entityTypeVal: String): String {
            return "camera_screen/$entityIdVal/$entityTypeVal"
        }
    }
}