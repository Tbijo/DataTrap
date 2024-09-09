package com.example.datatrap.settings.presentation

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.datatrap.core.presentation.LoadingScreen
import com.example.datatrap.core.presentation.components.GenericListItem
import com.example.datatrap.core.presentation.components.MyScaffold
import com.example.datatrap.core.presentation.components.MyTextField

@Composable
fun SettingsEntityScreen(
    onEvent: (SettingsEntityEvent) -> Unit,
    state: SettingsEntityUiState,
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
    onEvent: (SettingsEntityEvent) -> Unit,
    state: SettingsEntityUiState,
) {
    MyScaffold(
        title = state.title,
        errorState = state.error,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onEvent(
                        SettingsEntityEvent.OnInsertClick
                    )
                },
                shape = RoundedCornerShape(20.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = state.iconDescription,
                )
            }
        },
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            MyTextField(
                value = state.textNameValue,
                placeholder = state.placeholder,
                error = state.textNameError,
                label = state.label,
                onValueChanged = { text ->
                    onEvent(
                        SettingsEntityEvent.OnNameTextChanged(text)
                    )
                },
            )
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            horizontalAlignment = Alignment.Start,
        ) {

            items(state.entityList) {entity ->
                GenericListItem(
                    itemName = entity.entityName,
                    onItemClick = {
                        onEvent(
                            SettingsEntityEvent.OnItemClick(entity)
                        )
                    },
                    onDeleteClick = {
                        onEvent(
                            SettingsEntityEvent.OnDeleteClick(entity)
                        )
                    },
                )
            }
        }
    }
}