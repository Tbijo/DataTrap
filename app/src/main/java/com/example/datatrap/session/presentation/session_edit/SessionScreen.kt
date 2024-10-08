package com.example.datatrap.session.presentation.session_edit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.datatrap.core.presentation.LoadingScreen
import com.example.datatrap.core.presentation.components.KeyType
import com.example.datatrap.core.presentation.components.MyScaffold
import com.example.datatrap.core.presentation.components.MyTextField

@Composable
fun SessionScreen(
    onEvent: (SessionScreenEvent) -> Unit,
    state: SessionUiState,
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
    onEvent: (SessionScreenEvent) -> Unit,
    state: SessionUiState,
) {
    MyScaffold(
        title = "Session",
        errorState = state.error,
        actions = {
            IconButton(onClick = {
                onEvent(
                    SessionScreenEvent.OnInsertClick
                )
            }) {
                Icon(imageVector = Icons.Default.Save, contentDescription = "save icon")
            }
        }
    ) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(it)
        ) {
            MyTextField(
                value = state.sessionNum,
                keyType = KeyType.NUMBER,
                placeholder = "Session Number*",
                error = state.sessionNumError,
                label = "Session Number*",
                onValueChanged = { text ->
                    onEvent(
                        SessionScreenEvent.OnSessionNumberChange(text)
                    )
                }
            )
            MyTextField(
                value = state.numOcc,
                keyType = KeyType.NUMBER,
                placeholder = "Occasion Count*",
                error = state.numOccError,
                label = "Occasion Count*",
                onValueChanged = { text ->
                    onEvent(
                        SessionScreenEvent.OnNumberOccasionChange(text)
                    )
                }
            )
        }
    }
}