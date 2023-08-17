package com.example.datatrap.specie.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.datatrap.specie.presentation.specie_add_edit.SpecieScreen
import com.example.datatrap.specie.presentation.specie_detail.SpecieDetailScreen
import com.example.datatrap.specie.presentation.specie_image.SpecieImageScreen
import com.example.datatrap.specie.presentation.specie_list.SpecieListScreen

fun NavGraphBuilder.specieNavigation(navController: NavHostController) {

    composable(
        route = SpecieScreens.SpecieListScreen.route
    ) {
        SpecieListScreen(
            onEvent = {}
        )
    }

    composable(
        route = SpecieScreens.SpecieScreen.route
    ) {
        SpecieScreen(
            onEvent = {}
        )
    }

    composable(
        route = SpecieScreens.SpecieImageScreen.route
    ) {
        SpecieImageScreen(
            onEvent = {}
        )
    }

    composable(
        route = SpecieScreens.SpecieDetailScreen.route
    ) {
        SpecieDetailScreen(
            onEvent = {}
        )
    }

}