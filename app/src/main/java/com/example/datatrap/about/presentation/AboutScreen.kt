package com.example.datatrap.about.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.datatrap.core.presentation.LoadingScreen
import com.example.datatrap.core.presentation.components.DrawerScreens
import com.example.datatrap.core.presentation.components.NavigationScaffold

@Composable
fun AboutScreen(
    onEvent: (DrawerScreens) -> Unit,
    state: AboutUiState,
) {
    when(state.isLoading) {
        true -> LoadingScreen()
        false -> ScreenContent(
            onEvent = onEvent,
            state = state,
        )
    }
}

@Composable
private fun ScreenContent(
    onEvent: (DrawerScreens) -> Unit,
    state: AboutUiState,
) {
    NavigationScaffold(
        title = "About",
        errorState = state.error,
        onDrawerItemClick = {
            onEvent(it)
        },
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(it),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = "About Screen")
        }
    }
}