package com.example.datatrap.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.datatrap.core.presentation.components.DrawerScreens
import com.example.datatrap.core.presentation.components.MyScaffold
import com.example.datatrap.settings.navigation.SettingsScreens

enum class SettingsScreenNames(val route: String, val properName: String) {
    ENVIRONMENT(
        route = SettingsScreens.EnvListScreen.route,
        properName = "Environment Type",
    ),
    METHOD(
        route = SettingsScreens.MethodListScreen.route,
        properName = "Method",
    ),
    METHODTYPE(
        route = SettingsScreens.MethodTypeListScreen.route,
        properName = "Method Type",
    ),
    PROTOCOL(
        route = SettingsScreens.ProtocolListScreen.route,
        properName = "Protocol",
    ),
    TRAPTYPE(
        route = SettingsScreens.TrapTypeListScreen.route,
        properName = "Trap Type",
    ),
    USER(
        route = SettingsScreens.UserListScreen.route,
        properName = "Vegetation Type",
    ),
    VEGETTYPE(
        route = SettingsScreens.VegTypeListScreen.route,
        properName = "User",
    )
}

sealed interface SettingsListEvent {
    data class OnDrawerClick(val drawerScreens: DrawerScreens): SettingsListEvent
    data class OnListClick(val sreenName: SettingsScreenNames): SettingsListEvent
}

@Composable
fun SettingsListScreen(
    onEvent: (SettingsListEvent) -> Unit,
) {
    MyScaffold(
        title = "Settings",
        onDrawerItemClick = {
            onEvent(
                SettingsListEvent.OnDrawerClick(it)
            )
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            items(SettingsScreenNames.values()) { screen: SettingsScreenNames ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onEvent(
                                SettingsListEvent.OnListClick(screen)
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = screen.properName)
                }
            }
        }
    }
}
