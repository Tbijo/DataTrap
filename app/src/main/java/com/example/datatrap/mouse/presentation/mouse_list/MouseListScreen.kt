package com.example.datatrap.mouse.presentation.mouse_list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddBox
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
    MyScaffold(
        title = "Mouse List",
        errorState = state.error,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onEvent(
                        MouseListScreenEvent.OnAddButtonClick
                    )
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "add button",
                )
            }
        },
        actions = {
            IconButton(onClick = {
                onEvent(MouseListScreenEvent.OnMultiButtonClick)
            }) {
                Icon(imageVector = Icons.Default.AddBox, contentDescription = "Multi Add button")
            }
            TextButton(onClick = {
                onEvent(MouseListScreenEvent.OnRecaptureButtonClick)
            }) {
                Text(text = "Recapture")
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
        ) {
            LazyColumn {
                item {
                    Row {
                        Text(text = "Project Name: ${state.projectName}")
                        Text(text = "Locality Name: ${state.localityName}")
                        Text(text = "Session Num.: ${state.sessionNum}")
                        Text(text = "Occasion Num.: ${state.occasionNum}")
                    }
                }
                items(state.mouseList) { mouse ->
                    MouseListItem(
                        mouse = mouse,
                        onItemClick = {
                            onEvent(
                                MouseListScreenEvent.OnItemClick(mouse.mouseId)
                            )
                        },
                        onUpdateClick = {
                            onEvent(
                                MouseListScreenEvent.OnUpdateButtonClick(mouse.mouseId)
                            )
                        },
                        onDeleteClick = {
                            onEvent(
                                MouseListScreenEvent.OnDeleteClick(mouse.mouseId)
                            )
                        },
                    )
                }
            }
        }
    }
}