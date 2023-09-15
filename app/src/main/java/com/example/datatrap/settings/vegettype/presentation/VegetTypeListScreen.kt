package com.example.datatrap.settings.vegettype.presentation

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.datatrap.core.presentation.LoadingScreen
import com.example.datatrap.core.presentation.components.GenericListItem
import com.example.datatrap.core.presentation.components.MyScaffold
import com.example.datatrap.core.presentation.components.MyTextField

@Composable
fun VegetTypeListScreen(
    onEvent: (VegetTypeListScreenEvent) -> Unit,
    state: VegetTypeListUiState,
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
    onEvent: (VegetTypeListScreenEvent) -> Unit,
    state: VegetTypeListUiState,
) {
    MyScaffold(
        title = "Veget Type List",
        errorState = state.error,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onEvent(
                        VegetTypeListScreenEvent.OnInsertClick
                    )
                },
                shape = RoundedCornerShape(20.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = "Add Vegetation Type"
                )
            }
        },
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            MyTextField(
                value = state.textNameValue,
                placeholder = "Vegetation Type",
                error = state.textNameError,
                label = "Vegetation Type",
                onValueChanged = { text ->
                    onEvent(
                        VegetTypeListScreenEvent.OnNameTextChanged(text)
                    )
                }
            )
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            horizontalAlignment = Alignment.Start,
        ) {

            items(state.vegetTypeEntityList) { vegetType ->
                GenericListItem(
                    itemName = vegetType.vegetTypeName,
                    onItemClick = {
                        onEvent(
                            VegetTypeListScreenEvent.OnItemClick(vegetType)
                        )
                    },
                    onDeleteClick = {
                        VegetTypeListScreenEvent.OnDeleteClick(vegetType)
                    },
                )
            }
        }
    }
}