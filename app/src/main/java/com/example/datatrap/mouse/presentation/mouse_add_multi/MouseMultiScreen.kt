package com.example.datatrap.mouse.presentation.mouse_add_multi

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.datatrap.core.presentation.LoadingScreen
import com.example.datatrap.core.presentation.components.MyScaffold
import com.example.datatrap.mouse.presentation.mouse_add_multi.components.MouseMultiItem

@Composable
fun MouseMultiScreen(
    onEvent: () -> Unit,
    state: MouseMultiUiState,
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
    state: MouseMultiUiState,
) {
    MyScaffold(
        title = "Mouse Multi",
        errorState = state.error,
    ) {
        Column(modifier = Modifier.padding(it)) {
            Row {
                Button(onClick = { /*TODO*/ }) {
                    Text(text = "Add Row")
                }
                Button(onClick = { /*TODO*/ }) {
                    Text(text = "Remove Row")
                }
            }

            LazyColumn {
                item {
                    MouseMultiItem()
                }
            }
        }
    }
}