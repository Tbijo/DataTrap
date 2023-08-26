package com.example.datatrap.occasion.navigation

sealed class OccasionScreens(val route: String) {

    object OccasionListScreen: OccasionScreens("occasion_list_screen/{projectIdKey}/{localityIdKey}/{sessionIdKey}") {
        const val projectIdKey = "projectIdKey"
        const val localityIdKey = "localityIdKey"
        const val sessionIdKey = "sessionIdKey"

        fun passParams(projectIdVal: String, localityIdVal: String, sessionIdVal: String): String {
            return "occasion_list_screen/$projectIdVal/$localityIdVal/$sessionIdVal"
        }
    }

    object OccasionScreen: OccasionScreens("occasion_screen/{projectIdKey}/{localityIdKey}/{sessionIdKey}/{occasionIdKey}") {
        const val projectIdKey = "projectIdKey"
        const val localityIdKey = "localityIdKey"
        const val sessionIdKey = "sessionIdKey"
        const val occasionIdKey = "occasionIdKey"

        fun passParams(projectIdVal: String, localityIdVal: String, sessionIdVal: String, occasionIdKey: String): String {
            return "occasion_screen/$projectIdVal/$localityIdVal/$sessionIdVal/$occasionIdKey"
        }
    }

    object OccasionDetailScreen: OccasionScreens("occasion_detail_screen/{projectIdKey}/{localityIdKey}/{sessionIdKey}/{occasionIdKey}"){
        const val projectIdKey = "projectIdKey"
        const val localityIdKey = "localityIdKey"
        const val sessionIdKey = "sessionIdKey"
        const val occasionIdKey = "occasionIdKey"

        fun passParams(projectIdVal: String, localityIdVal: String, sessionIdVal: String, occasionIdKey: String): String {
            return "occasion_detail_screen/$projectIdVal/$localityIdVal/$sessionIdVal/$occasionIdKey"
        }
    }

}