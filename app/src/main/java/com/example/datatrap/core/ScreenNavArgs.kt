package com.example.datatrap.core

import android.os.Bundle
import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private const val NAV_ARGS_KEY = "navArgsKey"

fun setMainRouteWithArgs(route: String): String {
    return "$route/{$NAV_ARGS_KEY}"
}

fun NavController.navigateToMainScreen(route: String, screenNavArgs: ScreenNavArgs) {
    val json = screenNavArgs.toJson()
    navigate("$route/$json")
}

fun getMainScreenArguments(): List<NamedNavArgument> {
    return listOf(
        navArgument(name = NAV_ARGS_KEY) {
            defaultValue = ScreenNavArgs()
            nullable = false
            type = ScreenNavArgsType()
        }
    )
}

// get data in viewModel
fun SavedStateHandle.getMainScreenNavArgs(): ScreenNavArgs? = get<ScreenNavArgs>(NAV_ARGS_KEY)

// get data in navigation
fun NavBackStackEntry.getMainScreenNavArgs(): ScreenNavArgs? = arguments?.getParcelable(NAV_ARGS_KEY)

@Serializable
@Parcelize
data class ScreenNavArgs(
    val projectId: String? = null,
    val localityId: String? = null,
    val sessionId: String? = null,
    val occasionId: String? = null,
    val mouseId: String? = null,
    val primeMouseId: String? = null,
    val isRecapture: Boolean = false,
): Parcelable {
    fun toJson(): String {
        return Json.encodeToString(this)
    }
}

private class ScreenNavArgsType: NavType<ScreenNavArgs>(isNullableAllowed = false) {

    override fun get(bundle: Bundle, key: String): ScreenNavArgs? {
        return bundle.getParcelable(key)
    }
    override fun parseValue(value: String): ScreenNavArgs {
        return Json.decodeFromString<ScreenNavArgs>(value)
    }
    override fun put(bundle: Bundle, key: String, value: ScreenNavArgs) {
        bundle.putParcelable(key, value)
    }
}