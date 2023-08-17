package com.example.datatrap.project.navigation

sealed class ProjectScreens(val route: String) {
    object ProjectListScreen: ProjectScreens("project_list_screen")

    object ProjectScreen: ProjectScreens("project_screen/{projectIdKey}") {

        const val projectIdKey = "projectIdKey"

        fun passParams(projectIdVal: String): String {
            return "project_screen/$projectIdVal"
        }
    }
}