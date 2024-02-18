package com.example.datatrap.settings

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.datatrap.about.navigateToAboutScreen
import com.example.datatrap.core.presentation.components.DrawerScreens
import com.example.datatrap.project.navigateToProjectListScreen
import com.example.datatrap.settings.presentation.SettingsEntityScreen
import com.example.datatrap.settings.presentation.SettingsEntityViewModel
import com.example.datatrap.settings.user.presentation.UserListScreen
import com.example.datatrap.settings.user.presentation.UserViewModel
import com.example.datatrap.specie.navigateToSpecieListScreen
import com.example.datatrap.sync.navigateToSyncScreen

private const val SETTINGS_LIST_SCREEN_ROUTE = "settings_list_screen"
private const val SETT_ENTITY_SCREEN_ROUTE = "env_list_screen"
private const val USER_LIST_SCREEN_ROUTE = "user_list_screen"

private const val SETTINGS_ENTITY_KEY = "settings"

fun NavController.navigateToSettingsScreen() = navigate(SETTINGS_LIST_SCREEN_ROUTE)

private fun NavController.navigateToSettingsEntityScreen(settingsEntityScreen: SettingsScreenNames) {
    navigate("$SETT_ENTITY_SCREEN_ROUTE/$settingsEntityScreen")
}
private fun setSettingsEntityRouteWithArgs(): String {
    return "$SETT_ENTITY_SCREEN_ROUTE/{$SETTINGS_ENTITY_KEY}"
}
private fun getSettingsEntityScreenArgument(): List<NamedNavArgument> {
    return listOf(
        navArgument(name = SETTINGS_ENTITY_KEY) {
            nullable = false
            type = NavType.EnumType(SettingsScreenNames::class.java)
        }
    )
}
fun SavedStateHandle.getSettingEntityNavArg(): SettingsScreenNames? = get<SettingsScreenNames>(SETTINGS_ENTITY_KEY)

fun NavGraphBuilder.settingsNavigation(navController: NavHostController) {

    composable(route = SETTINGS_LIST_SCREEN_ROUTE) {
        SettingsListScreen(
            onEvent = { event ->
                when(event) {
                    is SettingsListEvent.OnDrawerClick -> {
                        when(event.drawerScreens) {
                            DrawerScreens.SPECIES -> {
                                navController.navigateToSpecieListScreen()
                            }
                            DrawerScreens.PROJECTS -> {
                                navController.navigateToProjectListScreen()
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
                    is SettingsListEvent.OnListClick -> {
                        if (event.sreenName == SettingsScreenNames.USER) {
                            navController.navigate(route = USER_LIST_SCREEN_ROUTE)
                        } else {
                            navController.navigateToSettingsEntityScreen(event.sreenName)
                        }
                    }
                }
            }
        )
    }

    composable(
        route = setSettingsEntityRouteWithArgs(),
        arguments = getSettingsEntityScreenArgument(),
    ) {
        val viewModel: SettingsEntityViewModel = hiltViewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()

        SettingsEntityScreen(
            onEvent = viewModel::onEvent,
            state = state,
        )
    }

    composable(route = USER_LIST_SCREEN_ROUTE) {
        val viewModel: UserViewModel = hiltViewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()

        UserListScreen(
            onEvent = viewModel::onEvent,
            state = state,
        )
    }

}