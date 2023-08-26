package com.example.datatrap.locality.navigation

sealed class LocalityScreens(val route: String) {

    object LocalityListScreen: LocalityScreens("locality_list_screen/{projectIdKey}") {

        const val projectIdKey = "projectIdKey"

        fun passParams(projectIdVal: String): String {
            return "locality_list_screen/$projectIdVal"
        }
    }

    object LocalityScreen: LocalityScreens("locality_screen/{localityIdKey}") {

        const val localityIdKey = "localityIdKey"

        fun passParams(localityIdVal: String): String {
            return "locality_screen/$localityIdVal"
        }
    }

    object LocalityMapScreen: LocalityScreens("locality_map_screen")
}