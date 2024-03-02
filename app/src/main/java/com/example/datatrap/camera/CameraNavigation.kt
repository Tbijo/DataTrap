package com.example.datatrap.camera

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
import com.example.datatrap.camera.presentation.CameraScreen
import com.example.datatrap.camera.presentation.CameraScreenEvent
import com.example.datatrap.camera.presentation.CameraViewModel
import com.example.datatrap.camera.util.EntityType

private const val CAMERA_SCREEN_ROUTE = "camera_screen"
private const val ENTITY_TYPE_KEY = "entityTypeKey"
private const val IMAGE_ID_KEY = "imageIdKey"
// to send to previous screen
private const val IMAGE_NAME_KEY = "imageNameKey"
private const val IMAGE_NOTE_KEY = "imageNoteKey"
private const val IMAGE_CHANGE_KEY = "imageChangeKey"

fun NavController.navigateToCameraScreen(entityType: EntityType, imageId: String?) {
    navigate("$CAMERA_SCREEN_ROUTE/$entityType/$imageId")
}
private fun setCameraRouteWithArgs(): String {
    return "$CAMERA_SCREEN_ROUTE/{$ENTITY_TYPE_KEY}/{$IMAGE_ID_KEY}"
}

fun SavedStateHandle.getEntityTypeNavArg(): EntityType? = get<EntityType>(ENTITY_TYPE_KEY)
fun SavedStateHandle.getImageIdNavArg(): String? = get<String>(IMAGE_ID_KEY)
private fun getCameraNavArgs(): List<NamedNavArgument> {
    return listOf(
        navArgument(name = ENTITY_TYPE_KEY) {
            nullable = false
            type = NavType.EnumType(EntityType::class.java)
        },
        navArgument(name = IMAGE_ID_KEY) {
            nullable = true
            type = NavType.StringType
        },
    )
}

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
    composable(
        route = setCameraRouteWithArgs(),
        arguments = getCameraNavArgs(),
    ) {
        val viewModel: CameraViewModel = hiltViewModel()
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