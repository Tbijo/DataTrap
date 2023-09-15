package com.example.datatrap.mouse.presentation.mouse_list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.datatrap.core.presentation.LoadingScreen
import com.example.datatrap.core.presentation.components.MyScaffold
import com.example.datatrap.mouse.presentation.mouse_list.components.MouseListItem

@Composable
fun MouseListScreen(
    onEvent: (MouseListScreenEvent) -> Unit,
    state: MouseListUiState,
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
    onEvent: (MouseListScreenEvent) -> Unit,
    state: MouseListUiState,
) {
    val project = "Patinnce"
    val locality = "Patince"
    val sessionNum = "12"
    val occasionNum = "12"

    MyScaffold(
        title = "Mouse List",
        errorState = state.error,
        floatingActionButton = {
            FloatingActionButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "add button",
                )
            }
        },
    ) {
        Column(modifier = Modifier.padding(it)) {

            Row {
                Text(text = "Project Name: $project")
                Text(text = "Locality Name: $locality")
                Text(text = "Session Num.: $sessionNum")
                Text(text = "Occasion Num.: $occasionNum")
            }

            LazyColumn {
                item {
                    MouseListItem()
                }
            }
        }
    }
}