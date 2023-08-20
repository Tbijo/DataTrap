package com.example.datatrap.settings.protocol.presentation

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.datatrap.core.presentation.components.GenericListItem
import com.example.datatrap.core.presentation.components.MyTextField

@Composable
fun ProtocolListScreen(
    onEvent: (ProtocolListScreenEvent) -> Unit,
    state: ProtocolListUiState,
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onEvent(
                        ProtocolListScreenEvent.OnInsertClick
                    )
                },
                shape = RoundedCornerShape(20.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = "Add Protocol"
                )
            }
        }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            MyTextField(
                value = state.textNameValue,
                placeholder = "Protocol",
                error = state.textNameError,
                label = "Protocol",
                onValueChanged = { text ->
                    onEvent(
                        ProtocolListScreenEvent.OnNameTextChanged(text)
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

            items(state.protocolEntityList) { protocol ->
                GenericListItem(
                    itemName = protocol.protocolName,
                    onItemClick = {
                        onEvent(
                            ProtocolListScreenEvent.OnItemClick(protocol)
                        )
                    },
                    onDeleteClick = {
                        onEvent(
                            ProtocolListScreenEvent.OnDeleteClick(protocol)
                        )
                    },
                )
            }
        }
    }
}