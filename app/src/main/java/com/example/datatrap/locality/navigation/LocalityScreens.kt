package com.example.datatrap.locality.navigation

sealed class LocalityScreens(val route: String) {

    object LocalityListScreen: LocalityScreens("locality_list_screen/{projectIdKey}") {

        const val projectIdKey = "projectIdKey"

        fun passParams(projectIdVal: String): String {
            return "project_screen/$projectIdVal"
        }
    }

    object LocalityScreen: LocalityScreens("locality_screen/{projectIdKey}") {

        const val projectIdKey = "projectIdKey"

        fun passParams(projectIdVal: String): String {
            return "project_screen/$projectIdVal"
        }
    }

    object LocalityMapScreen: LocalityScreens("locality_map_screen")
}