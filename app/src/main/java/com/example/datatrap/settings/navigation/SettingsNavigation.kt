package com.example.datatrap.settings.navigation

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.datatrap.about.navigation.AboutScreens
import com.example.datatrap.core.presentation.components.DrawerScreens
import com.example.datatrap.settings.SettingsListEvent
import com.example.datatrap.settings.SettingsListScreen
import com.example.datatrap.settings.envtype.presentation.EnvTypeListScreen
import com.example.datatrap.settings.envtype.presentation.EnvTypeViewModel
import com.example.datatrap.settings.method.presentation.MethodListScreen
import com.example.datatrap.settings.method.presentation.MethodViewModel
import com.example.datatrap.settings.methodtype.presentation.MethodTypeListScreen
import com.example.datatrap.settings.methodtype.presentation.MethodTypeViewModel
import com.example.datatrap.settings.protocol.presentation.ProtocolListScreen
import com.example.datatrap.settings.protocol.presentation.ProtocolViewModel
import com.example.datatrap.settings.traptype.presentation.TrapTypeListScreen
import com.example.datatrap.settings.traptype.presentation.TrapTypeViewModel
import com.example.datatrap.settings.user.presentation.UserListScreen
import com.example.datatrap.settings.user.presentation.UserViewModel
import com.example.datatrap.settings.vegettype.presentation.VegetTypeListScreen
import com.example.datatrap.settings.vegettype.presentation.VegetTypeViewModel
import com.example.datatrap.specie.navigation.SpecieScreens
import com.example.datatrap.sync.navigation.SyncScreens

fun NavGraphBuilder.settingsNavigation(navController: NavHostController) {

    composable(
        route = SettingsScreens.SettingsListScreen.route
    ) {
        SettingsListScreen(
            onEvent = { event ->
                when(event) {
                    is SettingsListEvent.OnDrawerClick -> {
                        when(event.drawerScreens) {
                            DrawerScreens.SPECIES -> {
                                navController.navigate(route = SpecieScreens.SpecieListScreen.route)
                            }
                            DrawerScreens.PROJECTS -> {
                                navController.navigate(route = SettingsScreens.ProtocolListScreen.route)
                            }
                            DrawerScreens.ABOUT -> {
                                navController.navigate(route = AboutScreens.AboutScreen.route)
                            }
                            DrawerScreens.SYNCHRONIZE -> {
                                navController.navigate(route = SyncScreens.SynchronizeScreen.route)
                            }
                            else -> Unit
                        }
                    }
                    is SettingsListEvent.OnListClick -> {
                        navController.navigate(route = event.sreenName.route)
                    }
                }
            }
        )
    }

    composable(
        route = SettingsScreens.EnvListScreen.route
    ) {
        val viewModel: EnvTypeViewModel = hiltViewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()

        EnvTypeListScreen(
            onEvent = viewModel::onEvent,
            state = state,
        )
    }

    composable(
        route = SettingsScreens.MethodListScreen.route
    ) {
        val viewModel: MethodViewModel = hiltViewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()

        MethodListScreen(
            onEvent = viewModel::onEvent,
            state = state,
        )
    }

    composable(
        route = SettingsScreens.MethodTypeListScreen.route
    ) {
        val viewModel: MethodTypeViewModel = hiltViewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()

        MethodTypeListScreen(
            onEvent = viewModel::onEvent,
            state = state,
        )
    }

    composable(
        route = SettingsScreens.ProtocolListScreen.route
    ) {
        val viewModel: ProtocolViewModel = hiltViewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()

        ProtocolListScreen(
            onEvent = viewModel::onEvent,
            state = state,
        )
    }

    composable(
        route = SettingsScreens.TrapTypeListScreen.route
    ) {
        val viewModel: TrapTypeViewModel = hiltViewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()

        TrapTypeListScreen(
            onEvent = viewModel::onEvent,
            state = state,
        )
    }

    composable(
        route = SettingsScreens.VegTypeListScreen.route
    ) {
        val viewModel: VegetTypeViewModel = hiltViewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()

        VegetTypeListScreen(
            onEvent = viewModel::onEvent,
            state = state,
        )
    }

    composable(
        route = SettingsScreens.UserListScreen.route
    ) {
        val viewModel: UserViewModel = hiltViewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()

        UserListScreen(
            onEvent = viewModel::onEvent,
            state = state,
        )
    }

}