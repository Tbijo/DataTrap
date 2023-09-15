package com.example.datatrap.mouse.presentation.mouse_recapture_list

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.datatrap.core.presentation.LoadingScreen
import com.example.datatrap.core.presentation.components.MyScaffold
import com.example.datatrap.mouse.presentation.mouse_recapture_list.components.RecaptureListItem

@Composable
fun RecaptureListScreen(
    onEvent: (RecaptureListScreenEvent) -> Unit,
    state: RecaptureListUiState,
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
    onEvent: (RecaptureListScreenEvent) -> Unit,
    state: RecaptureListUiState,
) {
    MyScaffold(
        title = "Recapture List",
        errorState = state.error,
        floatingActionButton = {
            FloatingActionButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "add icon")
            }
        }
    ) {
        LazyColumn(
            modifier = Modifier.padding(it)
        ) {
            item {
                RecaptureListItem()
            }
        }
    }
}