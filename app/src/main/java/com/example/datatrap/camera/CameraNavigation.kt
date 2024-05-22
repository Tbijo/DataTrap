package com.example.datatrap.camera

import android.os.Parcelable
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
import kotlinx.parcelize.Parcelize
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
@Parcelize
data class ReturnedImageData(
    val name: String?,
    val note: String?,
    val change: Boolean,
): Parcelable

private const val IMAGE_DATA_KEY = "image_data_key"

private fun NavHostController.setImageData(data: ReturnedImageData) {
    previousBackStackEntry?.savedStateHandle?.set(IMAGE_DATA_KEY, data)
}
fun NavHostController.getImageData(): ReturnedImageData? {
    return currentBackStackEntry?.savedStateHandle?.get<ReturnedImageData?>(IMAGE_DATA_KEY)
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
            },
        )
        val state by viewModel.state.collectAsStateWithLifecycle()

        CameraScreen(
            onEvent = { event ->
                when(event) {
                    is CameraScreenEvent.OnLeave -> {
                        navController.setImageData(
                            ReturnedImageData(
                                name = state.imageName,
                                note = state.note,
                                change = event.makeChange,
                            )
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