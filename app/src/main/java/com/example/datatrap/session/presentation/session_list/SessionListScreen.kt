package com.example.datatrap.session.presentation.session_list

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
import com.example.datatrap.session.presentation.session_list.components.SessionListItem

@Composable
fun SessionListScreen(
    onEvent: (SessionListScreenEvent) -> Unit,
    state: SessionListUiState,
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
    onEvent: (SessionListScreenEvent) -> Unit,
    state: SessionListUiState,
) {
    MyScaffold(
        title = "Session List",
        errorState = state.error,
        floatingActionButton = {
            FloatingActionButton(onClick = {
                onEvent(
                    SessionListScreenEvent.OnAddButtonClick
                )
            }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "add buton")
            }
        },
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            item {
                Row {
                    Text(text = "Project Name: ${state.projectName}")
                    Text(text = "Locality Name: ${state.localityName}")
                }
            }
            items(state.sessionList) {session ->
                SessionListItem(
                    sessionNumber = session.session,
                    numberOfOccasions = session.numOcc,
                    dateTime = session.sessionDateTimeCreated,
                    onItemClick = {
                        onEvent(
                            SessionListScreenEvent.OnItemClick(session, state.localityId)
                        )
                    },
                    onUpdateClick = {
                        onEvent(
                            SessionListScreenEvent.OnUpdateButtonClick(session)
                        )
                    },
                    onDeleteClick = {
                        onEvent(
                            SessionListScreenEvent.OnDeleteClick(session)
                        )
                    }
                )
            }
        }
    }
}