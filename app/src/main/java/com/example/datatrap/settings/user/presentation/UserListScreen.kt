package com.example.datatrap.settings.user.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
fun UserListScreen(
    onEvent: (UserScreenEvent) -> Unit,
    state: UserScreenUiState,
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
    onEvent: (UserScreenEvent) -> Unit,
    state: UserScreenUiState,
) {
    MyScaffold(
        title = "User List",
        errorState = state.error,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onEvent(
                        UserScreenEvent.OnInsertClick
                    )
                },
                shape = RoundedCornerShape(20.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = "Add User"
                )
            }
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                MyTextField(
                    value = state.textNameValue,
                    placeholder = "User Name",
                    error = state.textNameError,
                    label = "User Name",
                    onValueChanged = { text ->
                        onEvent(
                            UserScreenEvent.OnNameTextChanged(text)
                        )
                    }
                )

                MyTextField(
                    value = state.textPasswordValue,
                    placeholder = "Password",
                    error = state.textPasswordError,
                    label = "Password",
                    onValueChanged = { text ->
                        onEvent(
                            UserScreenEvent.OnPasswordChanged(text)
                        )
                    }
                )
            }
            LazyColumn(
                horizontalAlignment = Alignment.Start,
            ) {

                items(state.userEntityList) { user ->
                    GenericListItem(
                        itemName = user.userName,
                        onItemClick = {
                            onEvent(
                                UserScreenEvent.OnItemClick(user)
                            )
                        },
                        onDeleteClick = {
                            onEvent(
                                UserScreenEvent.OnDeleteClick(user)
                            )
                        },
                    )
                }
            }
        }
    }
}