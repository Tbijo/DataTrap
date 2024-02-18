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
import com.example.datatrap.camera.presentation.CameraViewModel

private const val CAMERA_SCREEN_ROUTE = "camera_screen"
private const val ENTITY_ID_KEY = "entityIdKey"
private const val ENTITY_TYPE_KEY = "entityTypeKey"

enum class EntityType {
    MOUSE, OCCASION;
}

fun NavController.navigateToCameraScreen(entityId: String, entityType: EntityType) {
    navigate("$CAMERA_SCREEN_ROUTE/$entityId/$entityType")
}

private fun setCameraRouteWithArgs(): String {
    return "$CAMERA_SCREEN_ROUTE/{$ENTITY_ID_KEY}/{$ENTITY_TYPE_KEY}"
}

fun SavedStateHandle.getEntityIdNavArg(): String? = get<String>(ENTITY_ID_KEY)
fun SavedStateHandle.getEntityTypeNavArg(): EntityType? = get<EntityType>(ENTITY_TYPE_KEY)

private fun getCameraNavArgs(): List<NamedNavArgument> {
    return listOf(
        navArgument(name = ENTITY_ID_KEY) {
            nullable = false
            type = NavType.StringType
        },
        navArgument(name = ENTITY_TYPE_KEY) {
            nullable = false
            type = NavType.EnumType(EntityType::class.java)
        },
    )
}

fun NavGraphBuilder.cameraNavigation(navController: NavHostController) {

    composable(
        route = setCameraRouteWithArgs(),
        arguments = getCameraNavArgs(),
    ) {
        val viewModel: CameraViewModel = hiltViewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()

        CameraScreen(
            onEvent = {},
            state = state,
        )
    }

}