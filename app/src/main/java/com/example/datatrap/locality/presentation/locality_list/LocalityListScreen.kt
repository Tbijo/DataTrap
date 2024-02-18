package com.example.datatrap.locality.presentation.locality_list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Map
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.datatrap.core.presentation.LoadingScreen
import com.example.datatrap.core.presentation.components.MyScaffold
import com.example.datatrap.core.presentation.components.SearchTextField
import com.example.datatrap.locality.presentation.locality_list.components.LocalityListItem

@Composable
fun LocalityListScreen(
    onEvent: (LocalityListScreenEvent) -> Unit,
    state: LocalityListUiState,
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
    onEvent: (LocalityListScreenEvent) -> Unit,
    state: LocalityListUiState,
) {
    MyScaffold(
        title = "Locality List",
        errorState = state.error,
        actions = {
            IconButton(onClick = {
                onEvent(
                    LocalityListScreenEvent.OnMapButtonCLick
                )
            }) {
                Icon(imageVector = Icons.Default.Map, contentDescription = "map icon")
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                onEvent(
                    LocalityListScreenEvent.OnAddButtonClick
                )
            }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "add icon")
            }
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            SearchTextField(
                text = state.searchTextFieldValue,
                hint = state.searchTextFieldHint,
                onValueChange = { text ->
                    onEvent(
                        LocalityListScreenEvent.OnSearchTextChange(text)
                    )
                },
                onFocusChange = { text ->
                    onEvent(
                        LocalityListScreenEvent.ChangeTitleFocus(text)
                    )
                },
                isHintVisible = state.isSearchTextFieldHintVisible,
            )

            LazyColumn {
                item {
                    Text(text = "Project Name: ${state.projectName}")
                }

                items(state.localityList) {locality ->
                    LocalityListItem(
                        localityEntity = locality,
                        onItemClick = {
                            state.projectId?.let {
                                onEvent(
                                    LocalityListScreenEvent.OnItemClick(
                                        localityId = locality.localityId,
                                        projectId = state.projectId,
                                    )
                                )
                            }
                        },
                        onUpdateClick = {
                            onEvent(
                                LocalityListScreenEvent.OnUpdateButtonClick(locality)
                            )
                        },
                        onDeleteClick = {
                            onEvent(
                                LocalityListScreenEvent.OnDeleteClick(locality)
                            )
                        }
                    )
                }

            }
        }
    }
}