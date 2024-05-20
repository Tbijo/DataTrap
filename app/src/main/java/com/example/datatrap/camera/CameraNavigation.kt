package com.example.datatrap.camera

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.datatrap.camera.presentation.CameraScreen
import com.example.datatrap.camera.presentation.CameraScreenEvent
import com.example.datatrap.camera.presentation.CameraViewModel
import com.example.datatrap.camera.util.EntityType
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import kotlin.reflect.typeOf

@Serializable
data class CameraScreenRoute(
    val entityType: EntityType,
    val entityId: String?,
)

// to send to previous screen
private const val IMAGE_NAME_KEY = "imageNameKey"
private const val IMAGE_NOTE_KEY = "imageNoteKey"
private const val IMAGE_CHANGE_KEY = "imageChangeKey"

// set imageName and note for previous screen
private fun NavHostController.setImageName(imageName: String?, imageNote: String?, makeChange: Boolean) {
    previousBackStackEntry?.savedStateHandle?.set(IMAGE_NAME_KEY, imageName)
    previousBackStackEntry?.savedStateHandle?.set(IMAGE_NOTE_KEY, imageNote)
    previousBackStackEntry?.savedStateHandle?.set(IMAGE_CHANGE_KEY, makeChange)
}
// get imageName and note from camera screen
fun NavHostController.getImageName(): String? {
    return currentBackStackEntry?.savedStateHandle?.get(IMAGE_NAME_KEY)
}
fun NavHostController.getImageNote(): String? {
    return currentBackStackEntry?.savedStateHandle?.get(IMAGE_NOTE_KEY)
}
fun NavHostController.getImageChange(): Boolean? {
    return currentBackStackEntry?.savedStateHandle?.get(IMAGE_CHANGE_KEY)
}
fun NavHostController.clearImageChange() {
    currentBackStackEntry?.savedStateHandle?.set(IMAGE_CHANGE_KEY, null)
}

fun NavGraphBuilder.cameraNavigation(navController: NavHostController) {
    composable<CameraScreenRoute>(
        typeMap = mapOf(typeOf<EntityType>() to NavType.EnumType(EntityType::class.java)),
    ) {
        val args = it.toRoute<CameraScreenRoute>()
        val viewModel: CameraViewModel = koinViewModel(
            parameters = {
                parametersOf(
                    args.entityType,
                    args.entityId,
                )
            }
        )
        val state by viewModel.state.collectAsStateWithLifecycle()

        CameraScreen(
            onEvent = { event ->
                when(event) {
                    is CameraScreenEvent.OnLeave -> {
                        navController.setImageName(
                            imageName = state.imageName,
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

}