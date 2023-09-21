package com.example.datatrap.mouse.presentation.mouse_recapture

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.datatrap.core.presentation.LoadingScreen
import com.example.datatrap.core.presentation.components.MyScaffold

@Composable
fun RecaptureScreen(
    onEvent: () -> Unit,
    state: RecaptureUiState,
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
    onEvent: () -> Unit,
    state: RecaptureUiState,
) {
    MyScaffold(
        title = "Recapture",
        errorState = state.error,
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(it)
        ) {

        }
    }
}