package com.example.datatrap.occasion.presentation.occasion_list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.datatrap.core.presentation.LoadingScreen
import com.example.datatrap.core.presentation.components.MyScaffold
import com.example.datatrap.occasion.presentation.occasion_list.components.OccasionListItem

@Composable
fun OccasionListScreen(
    onEvent: (OccasionListScreenEvent) -> Unit,
    state: OccasionListUiState,
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
    onEvent: (OccasionListScreenEvent) -> Unit,
    state: OccasionListUiState,
) {
    MyScaffold(
        title = "Occasion List",
        errorState = state.error,
        floatingActionButton = {
            FloatingActionButton(onClick = {
                onEvent(
                    OccasionListScreenEvent.OnAddButtonClick
                )
            }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "add buton")
            }
        },
    ) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(it)
        ) {
            Row {
                Text(text = "Project Name: ${state.projectName}")
                Text(text = "Locality Name: ${state.localityName}")
                Text(text = "Session Num: ${state.sessionNum}")
            }

            LazyColumn {
                items(state.occasionList) {occasion ->
                    OccasionListItem(
                        occasionEntity = occasion,
                        onItemClick = {
                            onEvent(
                                OccasionListScreenEvent.OnItemClick(occasion)
                            )
                        },
                        onDetailClick = {
                            onEvent(
                                OccasionListScreenEvent.OnDetailButtonClick(occasion)
                            )
                        },
                        onUpdateClick = {
                            onEvent(
                                OccasionListScreenEvent.OnUpdateButtonClick(occasion)
                            )
                        },
                        onDeleteClick = {
                            onEvent(
                                OccasionListScreenEvent.OnDeleteClick(occasion)
                            )
                        },
                    )
                }
            }
        }
    }
}