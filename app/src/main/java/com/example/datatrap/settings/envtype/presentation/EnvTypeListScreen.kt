package com.example.datatrap.settings.envtype.presentation

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
fun EnvTypeListScreen(
    onEvent: (EnvTypeListScreenEvent) -> Unit,
    state: EnvTypeListUiState,
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
    onEvent: (EnvTypeListScreenEvent) -> Unit,
    state: EnvTypeListUiState,
) {
    MyScaffold(
        title = "EnvType List",
        errorState = state.error,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onEvent(
                        EnvTypeListScreenEvent.OnInsertClick
                    )
                },
                shape = RoundedCornerShape(20.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = "Add EnvType"
                )
            }
        },
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            MyTextField(
                value = state.textNameValue,
                placeholder = "Environment Type",
                error = state.textNameError,
                label = "Environment Type",
                onValueChanged = { text ->
                    onEvent(
                        EnvTypeListScreenEvent.OnNameTextChanged(text)
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

            items(state.envTypeEntityList) {envType ->
                GenericListItem(
                    itemName = envType.envTypeName,
                    onItemClick = {
                        onEvent(
                            EnvTypeListScreenEvent.OnItemClick(envType)
                        )
                    },
                    onDeleteClick = {
                        onEvent(
                            EnvTypeListScreenEvent.OnDeleteClick(envType)
                        )
                    },
                )
            }
        }
    }
}