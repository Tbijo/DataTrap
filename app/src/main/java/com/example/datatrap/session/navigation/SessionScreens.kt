package com.example.datatrap.session.navigation

sealed class SessionScreens(val route: String) {
    object SessionListScreen: SessionScreens("session_list_screen/{projectIdKey}/{localityIdKey}") {

        const val projectIdKey = "projectIdKey"
        const val localityIdKey = "localityIdKey"

        fun passParams(projectIdVal: String, localityIdVal: String): String {
            return "session_list_screen/$projectIdVal/$localityIdVal"
        }
    }

    object SessionScreen: SessionScreens("session_screen/{sessionIdKey}") {

        const val sessionIdKey = "sessionIdKey"

        fun passParams(sessionIdVal: String?): String {
            return "session_screen/$sessionIdVal"
        }
    }
}