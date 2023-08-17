package com.example.datatrap.session.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.datatrap.session.presentation.session_edit.SessionScreen
import com.example.datatrap.session.presentation.session_prj_list.SessionListScreen

fun NavGraphBuilder.sessionNavigation(navController: NavHostController) {
    composable(
        route = SessionScreens.SessionListScreen.route
    ) {
        SessionListScreen(
            onEvent = {},
        )
    }

    composable(
        route = SessionScreens.SessionScreen.route
    ) {
        SessionScreen(
            onEvent = {},
        )
    }
}