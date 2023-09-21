package com.example.datatrap.mouse.navigation

sealed class MouseScreens(val route: String) {

    object MouseListScreen: MouseScreens("mouse_list_screen/{projectIdKey}/{localityIdKey}/{sessionIdKey}/{occasionIdKey}") {
        const val projectIdKey = "projectIdKey"
        const val localityIdKey = "localityIdKey"
        const val sessionIdKey = "sessionIdKey"
        const val occasionIdKey = "occasionIdKey"

        fun passParams(projectIdVal: String, localityIdVal: String, sessionIdVal: String, occasionIdVal: String): String {
            return "mouse_list_screen/$projectIdVal/$localityIdVal/$sessionIdVal/$occasionIdVal"
        }
    }

    object MouseScreen: MouseScreens("mouse_screen/{localityIdKey}/{occasionIdKey}/{mouseIdKey}") {
        const val localityIdKey = "localityIdKey"
        const val occasionIdKey = "occasionIdKey"
        const val mouseIdKey = "mouseIdKey"

        fun passParams(localityIdVal: String, occasionIdVal: String, mouseIdVal: String): String {
            return "mouse_screen/$localityIdVal/$occasionIdVal/$mouseIdVal"
        }
    }

    object MouseMultiScreen: MouseScreens("mouse_multi_screen/{localityIdKey}/{occasionIdKey}") {
        const val localityIdKey = "localityIdKey"
        const val occasionIdKey = "occasionIdKey"

        fun passParams(localityIdVal: String, occasionIdVal: String): String {
            return "mouse_multi_screen/$localityIdVal/$occasionIdVal"
        }
    }

    object MouseDetailScreen: MouseScreens("mouse_detail_screen/{localityIdKey}/{occasionIdKey}/{mouseIdKey}") {
        const val localityIdKey = "localityIdKey"
        const val occasionIdKey = "occasionIdKey"
        const val mouseIdKey = "mouseIdKey"

        fun passParams(localityIdVal: String, occasionIdVal: String, mouseIdVal: String): String {
            return "mouse_detail_screen/$localityIdVal/$occasionIdVal/$mouseIdVal"
        }
    }

    object MouseRecaptureScreen: MouseScreens("mouse_recap_screen/{localityIdKey}/{occasionIdKey}") {
        const val localityIdKey = "localityIdKey"
        const val occasionIdKey = "occasionIdKey"

        fun passParams(localityIdVal: String, occasionIdVal: String): String {
            return "mouse_recap_screen/$localityIdVal/$occasionIdVal"
        }
    }

    object MouseRecaptureListScreen: MouseScreens("mouse_recap_list_screen/{localityIdKey}/{occasionIdKey}") {
        const val localityIdKey = "localityIdKey"
        const val occasionIdKey = "occasionIdKey"

        fun passParams(localityIdVal: String, occasionIdVal: String): String {
            return "mouse_recap_list_screen/$localityIdVal/$occasionIdVal"
        }
    }

}