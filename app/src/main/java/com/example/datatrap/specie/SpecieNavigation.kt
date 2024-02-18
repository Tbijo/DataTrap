package com.example.datatrap.specie

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.datatrap.about.navigateToAboutScreen
import com.example.datatrap.core.presentation.components.DrawerScreens
import com.example.datatrap.project.navigateToProjectListScreen
import com.example.datatrap.settings.navigateToSettingsScreen
import com.example.datatrap.specie.presentation.specie_add_edit.SpecieScreen
import com.example.datatrap.specie.presentation.specie_add_edit.SpecieScreenEvent
import com.example.datatrap.specie.presentation.specie_add_edit.SpecieViewModel
import com.example.datatrap.specie.presentation.specie_detail.SpecieDetailScreen
import com.example.datatrap.specie.presentation.specie_detail.SpecieDetailViewModel
import com.example.datatrap.specie.presentation.specie_image.SpecieImageScreen
import com.example.datatrap.specie.presentation.specie_image.SpecieImageViewModel
import com.example.datatrap.specie.presentation.specie_list.SpecieListScreen
import com.example.datatrap.specie.presentation.specie_list.SpecieListScreenEvent
import com.example.datatrap.specie.presentation.specie_list.SpecieListViewModel
import com.example.datatrap.sync.navigateToSyncScreen

private const val SPECIE_LIST_SCREEN_ROUTE = "specie_list_screen"
private const val SPECIE_SCREEN_ROUTE = "specie_screen"
private const val SPECIE_IMAGE_SCREEN_ROUTE = "specie_image_screen"
private const val SPECIE_DETAIL_SCREEN_ROUTE = "specie_detail_screen"

private const val SPECIE_ID_KEY = "specieIdKey"

fun NavController.navigateToSpecieListScreen() = navigate(SPECIE_LIST_SCREEN_ROUTE)
private fun getSpecieScreenArguments(): List<NamedNavArgument> {
    return listOf(
        navArgument(name = SPECIE_ID_KEY) {
            nullable = true
            type = NavType.StringType
            defaultValue = null
        }
    )
}
fun SavedStateHandle.getSpecieIdArg(): String? = get(SPECIE_ID_KEY)
private fun NavBackStackEntry.getSpecieIdArg(): String? = arguments?.getString(SPECIE_ID_KEY)
private fun setSpecieRouteWithArgs(route: String) = "$route/{$SPECIE_ID_KEY}"

private fun NavController.navigateToAnySpecieScreen(route: String, specieId: String?) {
    navigate("$route/$specieId")
}
private fun NavController.navigateToSpecieScreen(specieId: String?) {
    navigateToAnySpecieScreen(SPECIE_SCREEN_ROUTE, specieId)
}
private fun NavController.navigateToSpecieImageScreen(specieId: String?) {
    navigateToAnySpecieScreen(SPECIE_IMAGE_SCREEN_ROUTE, specieId)
}
private fun NavController.navigateToSpecieDetailScreen(specieId: String) {
    navigateToAnySpecieScreen(SPECIE_DETAIL_SCREEN_ROUTE, specieId)
}

fun NavGraphBuilder.specieNavigation(navController: NavHostController) {

    composable(route = SPECIE_LIST_SCREEN_ROUTE) {
        val viewModel: SpecieListViewModel = hiltViewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()

        SpecieListScreen(
            onEvent = {
                when(it) {
                    SpecieListScreenEvent.OnAddButtonClick -> {
                        navController.navigateToSpecieScreen(
                            specieId = null,
                        )
                    }

                    is SpecieListScreenEvent.OnItemClick -> {
                        navController.navigateToSpecieDetailScreen(
                            specieId = it.specieEntity.specieId,
                        )
                    }

                    is SpecieListScreenEvent.OnDrawerItemClick -> {
                        when(it.drawerScreen) {
                            DrawerScreens.PROJECTS -> {
                                navController.navigateToProjectListScreen()
                            }
                            DrawerScreens.SETTINGS -> {
                                navController.navigateToSettingsScreen()
                            }
                            DrawerScreens.ABOUT -> {
                                navController.navigateToAboutScreen()
                            }
                            DrawerScreens.SYNCHRONIZE -> {
                                navController.navigateToSyncScreen()
                            }
                            else -> Unit
                        }
                    }

                    else -> viewModel.onEvent(it)
                }
            },
            state = state,
        )
    }

    composable(
        route = setSpecieRouteWithArgs(SPECIE_SCREEN_ROUTE),
        arguments = getSpecieScreenArguments(),
    ) {
        val viewModel: SpecieViewModel = hiltViewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()

        val specieId = it.getSpecieIdArg()

        SpecieScreen(
            onEvent = { event ->
                when(event) {
                    SpecieScreenEvent.OnCameraClick -> {
                        navController.navigateToSpecieImageScreen(
                            specieId = specieId,
                        )
                    }

                    else -> viewModel.onEvent(event)
                }
            },
            state = state,
        )
    }

    composable(
        route = setSpecieRouteWithArgs(SPECIE_IMAGE_SCREEN_ROUTE),
        arguments = getSpecieScreenArguments(),
    ) {
        val viewModel: SpecieImageViewModel = hiltViewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()

        SpecieImageScreen(
            onEvent = viewModel::onEvent,
            state = state,
        )
    }

    composable(
        route = setSpecieRouteWithArgs(SPECIE_DETAIL_SCREEN_ROUTE),
        arguments = getSpecieScreenArguments(),
    ) {
        val viewModel: SpecieDetailViewModel = hiltViewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()

        SpecieDetailScreen(
            onEvent = viewModel::onEvent,
            state = state,
        )
    }

}