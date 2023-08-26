package com.example.datatrap.locality.presentation.locality_list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.datatrap.locality.presentation.locality_list.components.LocalityListItem

@Composable
fun LocalityListScreen(
    onEvent: (LocalityListScreenEvent) -> Unit,
    state: LocalityListUiState,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Locality List")
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                onEvent(
                    LocalityListScreenEvent.OnAddButtonClick
                )
            }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "add icon")
            }
        }
    ) {
        Column(
            modifier = Modifier.padding(it)
        ) {
            LazyColumn {
                item {
                    Text(text = "Project Name: ${state.projectName}")
                }

                items(state.localityList) {locality ->
                    LocalityListItem(
                        localityEntity = locality,
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