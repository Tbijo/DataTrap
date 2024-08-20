package com.example.datatrap.settings

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.datatrap.about.AboutScreenRoute
import com.example.datatrap.core.presentation.components.DrawerScreens
import com.example.datatrap.project.ProjectListScreenRoute
import com.example.datatrap.settings.presentation.SettingsEntityScreen
import com.example.datatrap.settings.presentation.SettingsEntityViewModel
import com.example.datatrap.settings.user.presentation.UserListScreen
import com.example.datatrap.settings.user.presentation.UserViewModel
import com.example.datatrap.specie.SpecieListScreenRoute
import com.example.datatrap.sync.SyncScreenRoute
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import kotlin.reflect.typeOf

@Serializable
object SettingsListScreenRoute
@Serializable
data class SettingsEntityScreenRoute(
    val settingsEntity: SettingsScreenNames,
)
@Serializable
object UserListScreenRoute

fun NavGraphBuilder.settingsNavigation(navController: NavHostController) {
    composable<SettingsListScreenRoute> {
        SettingsListScreen(
            onEvent = { event ->
                when(event) {
                    is SettingsListEvent.OnDrawerClick -> {
                        when(event.drawerScreens) {
                            DrawerScreens.SPECIES -> {
                                navController.navigate(SpecieListScreenRoute)
                            }
                            DrawerScreens.PROJECTS -> {
                                navController.navigate(ProjectListScreenRoute)
                            }
                            DrawerScreens.ABOUT -> {
                                navController.navigate(AboutScreenRoute)
                            }
                            DrawerScreens.SYNCHRONIZE -> {
                                navController.navigate(SyncScreenRoute)
                            }
                            else -> Unit
                        }
                    }
                    is SettingsListEvent.OnListClick -> {
                        if (event.sreenName == SettingsScreenNames.USER) {
                            navController.navigate(UserListScreenRoute)
                        } else {
                            navController.navigate(
                                SettingsEntityScreenRoute(
                                    settingsEntity = event.sreenName,
                                )
                            )
                        }
                    }
                }
            }
        )
    }

    composable<SettingsEntityScreenRoute>(
        typeMap = mapOf(typeOf<SettingsScreenNames>() to NavType.EnumType(SettingsScreenNames::class.java)),
    ) {
        val args = it.toRoute<SettingsEntityScreenRoute>()
        val viewModel: SettingsEntityViewModel = koinViewModel(
            parameters = {
                parametersOf(
                    args.settingsEntity,
                )
            },
        )
        val state by viewModel.state.collectAsStateWithLifecycle()

        SettingsEntityScreen(
            onEvent = viewModel::onEvent,
            state = state,
        )
    }

    composable<UserListScreenRoute> {
        val viewModel: UserViewModel = koinViewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()

        UserListScreen(
            onEvent = viewModel::onEvent,
            state = state,
        )
    }

}