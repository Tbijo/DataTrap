package com.example.datatrap.settings.methodtype.presentation

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
fun MethodTypeListScreen(
    onEvent: (MethodTypeListScreenEvent) -> Unit,
    state: MethodTypeListUiState,
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onEvent(
                        MethodTypeListScreenEvent.OnInsertClick
                    )
                },
                shape = RoundedCornerShape(20.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = "Add Method Type"
                )
            }
        }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            MyTextField(
                value = state.textNameValue,
                placeholder = "Method Type",
                error = state.textNameError,
                label = "Method Type",
                onValueChanged = { text ->
                    onEvent(
                        MethodTypeListScreenEvent.OnNameTextChanged(text)
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

            items(state.methodTypeEntityList) { methodType ->
                GenericListItem(
                    itemName = methodType.methodTypeName,
                    onItemClick = {
                        onEvent(
                            MethodTypeListScreenEvent.OnItemClick(methodType)
                        )
                    },
                    onDeleteClick = {
                        onEvent(
                            MethodTypeListScreenEvent.OnDeleteClick(methodType)
                        )
                    },
                )
            }
        }
    }
}