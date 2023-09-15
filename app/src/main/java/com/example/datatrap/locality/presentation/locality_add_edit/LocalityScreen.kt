package com.example.datatrap.locality.presentation.locality_add_edit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.datatrap.core.presentation.LoadingScreen
import com.example.datatrap.core.presentation.components.MyScaffold
import com.example.datatrap.core.presentation.components.MyTextField

@Composable
fun LocalityScreen(
    onEvent: (LocalityScreenEvent) -> Unit,
    state: LocalityUiState,
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
    onEvent: (LocalityScreenEvent) -> Unit,
    state: LocalityUiState,
) {
    MyScaffold(
        title = "Locality",
        errorState = state.error,
        actions = {
            IconButton(onClick = {
                onEvent(
                    LocalityScreenEvent.OnInsertClick
                )
            }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "save locality")
            }
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            MyTextField(value = state.localityName, placeholder = "Topolcany", error = state.localityNameError, label = "Locality Name*",
                onValueChanged = { text ->
                    onEvent(
                        LocalityScreenEvent.OnLocalityNameChange(text)
                    )
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            MyTextField(value = state.numSessions, placeholder = "12", error = null, label = "Session Count*",
                onValueChanged = { text ->
                    onEvent(
                        LocalityScreenEvent.OnNumSessionsChange(text)
                    )
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row {
                MyTextField(value = state.latitudeA, placeholder = "12.12", error = null, label = "Latitude A",
                    onValueChanged = { text ->
                        onEvent(
                            LocalityScreenEvent.OnLatitudeAChange(text)
                        )
                    }
                )

                MyTextField(value = state.longitudeA, placeholder = "12.11", error = null, label = "Longitude A",
                    onValueChanged = { text ->
                        onEvent(
                            LocalityScreenEvent.OnLongitudeAChange(text)
                        )
                    }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(onClick = {
                onEvent(
                    LocalityScreenEvent.OnButtonCoorAClick
                )
            }) {
                Text(text = "Get Coordinates A")
            }

            Spacer(modifier = Modifier.height(32.dp))

            Row {
                MyTextField(value = state.latitudeB, placeholder = "12.12", error = null, label = "Latitude B",
                    onValueChanged = { text ->
                        onEvent(
                            LocalityScreenEvent.OnLatitudeBChange(text)
                        )
                    }
                )

                MyTextField(value = state.longitudeB, placeholder = "12.11", error = null, label = "Longitude B",
                    onValueChanged = { text ->
                        onEvent(
                            LocalityScreenEvent.OnLongitudeBChange(text)
                        )
                    }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(onClick = {
                onEvent(
                    LocalityScreenEvent.OnButtonCoorBClick
                )
            }) {
                Text(text = "Get Coordinates B")
            }

            Spacer(modifier = Modifier.height(32.dp))

            MyTextField(value = state.note, placeholder = "Note... note", error = null, label = "Note",
                onValueChanged = { text ->
                    onEvent(
                        LocalityScreenEvent.OnNoteChange(text)
                    )
                }
            )
        }
    }
}