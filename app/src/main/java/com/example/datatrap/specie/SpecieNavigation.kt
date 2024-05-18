package com.example.datatrap.specie

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.datatrap.about.AboutScreenRoute
import com.example.datatrap.core.presentation.components.DrawerScreens
import com.example.datatrap.project.ProjectListScreenRoute
import com.example.datatrap.settings.SettingsListScreenRoute
import com.example.datatrap.specie.presentation.specie_add_edit.SpecieScreen
import com.example.datatrap.specie.presentation.specie_add_edit.SpecieScreenEvent
import com.example.datatrap.specie.presentation.specie_add_edit.SpecieViewModel
import com.example.datatrap.specie.presentation.specie_detail.SpecieDetailScreen
import com.example.datatrap.specie.presentation.specie_detail.SpecieDetailViewModel
import com.example.datatrap.specie.presentation.specie_image.SpecieImageScreen
import com.example.datatrap.specie.presentation.specie_image.SpecieImageScreenEvent
import com.example.datatrap.specie.presentation.specie_image.SpecieImageViewModel
import com.example.datatrap.specie.presentation.specie_list.SpecieListScreen
import com.example.datatrap.specie.presentation.specie_list.SpecieListScreenEvent
import com.example.datatrap.specie.presentation.specie_list.SpecieListViewModel
import com.example.datatrap.sync.SyncScreenRoute
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Serializable
object SpecieListScreenRoute
@Serializable
data class SpecieScreenRoute(
    val specieId: String?,
)
@Serializable
data class SpecieImageScreenRoute(
    val specieId: String?,
)
@Serializable
data class SpecieDetailScreenRoute(
    val specieId: String,
)

// to send to previous screen
private const val IMAGE_URI_KEY = "imageUriKey"
private const val IMAGE_NOTE_KEY = "imageNoteKey"
private const val IMAGE_CHANGE_KEY = "imageChangeKey"

// set imageName and note for previous screen
private fun NavHostController.setImageName(imageUri: String?, imageNote: String?, makeChange: Boolean) {
    previousBackStackEntry?.savedStateHandle?.set(IMAGE_URI_KEY, imageUri)
    previousBackStackEntry?.savedStateHandle?.set(IMAGE_NOTE_KEY, imageNote)
    previousBackStackEntry?.savedStateHandle?.set(IMAGE_CHANGE_KEY, makeChange)
}
// get imageName and note from camera screen
private fun NavHostController.getImageName(): String? {
    return currentBackStackEntry?.savedStateHandle?.get(IMAGE_URI_KEY)
}
private fun NavHostController.getImageNote(): String? {
    return currentBackStackEntry?.savedStateHandle?.get(IMAGE_NOTE_KEY)
}
private fun NavHostController.getImageChange(): Boolean? {
    return currentBackStackEntry?.savedStateHandle?.get(IMAGE_CHANGE_KEY)
}
private fun NavHostController.clearImageChange() {
    currentBackStackEntry?.savedStateHandle?.set(IMAGE_CHANGE_KEY, null)
}

fun NavGraphBuilder.specieNavigation(navController: NavHostController) {

    composable<SpecieListScreenRoute> {
        val viewModel: SpecieListViewModel = koinViewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()

        SpecieListScreen(
            onEvent = { event ->
                when(event) {
                    SpecieListScreenEvent.OnAddButtonClick -> {
                        navController.navigate(
                            SpecieScreenRoute(
                                specieId = null,
                            )
                        )
                    }

                    is SpecieListScreenEvent.OnItemClick -> {
                        navController.navigate(
                            SpecieDetailScreenRoute(
                                specieId = event.specieEntity.specieId,
                            )
                        )
                    }

                    is SpecieListScreenEvent.OnDrawerItemClick -> {
                        when(event.drawerScreen) {
                            DrawerScreens.PROJECTS -> {
                                navController.navigate(ProjectListScreenRoute)
                            }
                            DrawerScreens.SETTINGS -> {
                                navController.navigate(SettingsListScreenRoute)
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

                    else -> viewModel.onEvent(event)
                }
            },
            state = state,
        )
    }

    composable<SpecieScreenRoute> {
        val args = it.toRoute<SpecieScreenRoute>()
        val viewModel: SpecieViewModel = koinViewModel(
            parameters = {
                parametersOf(args.specieId)
            }
        )
        val state by viewModel.state.collectAsStateWithLifecycle()
        val imageName = navController.getImageName()
        val imageNote = navController.getImageNote()
        val makeChange = navController.getImageChange()

        LaunchedEffect(key1 = imageName, key2 = imageNote, key3 = makeChange) {
            if (makeChange == true) {
                viewModel.onEvent(
                    SpecieScreenEvent.OnReceiveImageName(
                        imageName = imageName,
                        imageNote = imageNote,
                    )
                )
                // clear for config change
                navController.clearImageChange()
            }
        }

        SpecieScreen(
            onEvent = { event ->
                when(event) {
                    SpecieScreenEvent.OnCameraClick -> {
                        navController.navigate(
                            SpecieImageScreenRoute(
                                specieId = args.specieId,
                            )
                        )
                    }
                    else -> viewModel.onEvent(event)
                }
            },
            state = state,
        )
    }

    composable<SpecieImageScreenRoute> {
        val args = it.toRoute<SpecieImageScreenRoute>()
        val viewModel: SpecieImageViewModel = koinViewModel(
            parameters = {
                parametersOf(args.specieId)
            }
        )
        val state by viewModel.state.collectAsStateWithLifecycle()

        SpecieImageScreen(
            onEvent = { event ->
                when(event) {
                    is SpecieImageScreenEvent.OnLeave -> {
                        navController.setImageName(
                            imageUri = state.imageUri.toString(),
                            imageNote = state.note,
                            makeChange = event.makeChange,
                        )
                        navController.navigateUp()
                    }
                    else -> viewModel.onEvent(event)
                }
            },
            state = state,
        )
    }

    composable<SpecieDetailScreenRoute> {
        val args = it.toRoute<SpecieDetailScreenRoute>()
        val viewModel: SpecieDetailViewModel = koinViewModel(
            parameters = {
                parametersOf(args.specieId)
            }
        )
        val state by viewModel.state.collectAsStateWithLifecycle()

        SpecieDetailScreen(
            onEvent = viewModel::onEvent,
            state = state,
        )
    }

}