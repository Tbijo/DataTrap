package com.example.datatrap.occasion.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.datatrap.occasion.presentation.occasion_add_edit.OccasionScreen
import com.example.datatrap.occasion.presentation.occasion_detail.OccasionDetailScreen
import com.example.datatrap.occasion.presentation.occasion_list.OccasionListScreen

fun NavGraphBuilder.occasionNavigation(navController: NavHostController) {

    composable(
        route = OccasionScreens.OccasionListScreen.route
    ) {
        OccasionListScreen(
            onEvent = {},
        )
    }

    composable(
        route = OccasionScreens.OccasionScreen.route
    ) {
        OccasionScreen(
            onEvent = {},
        )
    }

    composable(
        route = OccasionScreens.OccasionDetailScreen.route
    ) {
        OccasionDetailScreen(
            onEvent = {},
        )
    }
}