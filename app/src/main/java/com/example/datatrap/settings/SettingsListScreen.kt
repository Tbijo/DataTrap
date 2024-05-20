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
import com.example.datatrap.core.presentation.components.NavigationScaffold

enum class SettingsScreenNames(val properName: String) {
    ENVIRONMENT(
        properName = "Environment Type",
    ),
    METHOD(
        properName = "Method",
    ),
    METHODTYPE(
        properName = "Method Type",
    ),
    PROTOCOL(
        properName = "Protocol",
    ),
    TRAPTYPE(
        properName = "Trap Type",
    ),
    USER(
        properName = "Vegetation Type",
    ),
    VEGETTYPE(
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
    NavigationScaffold(
        title = "Settings",
        errorState = null,
        onDrawerItemClick = {
            onEvent(
                SettingsListEvent.OnDrawerClick(it)
            )
        },
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
