package com.example.datatrap.project.presentation.project_edit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.datatrap.core.presentation.LoadingScreen
import com.example.datatrap.core.presentation.components.KeyType
import com.example.datatrap.core.presentation.components.MyTextField

@Composable
fun ProjectScreen(
    onEvent: (ProjectScreenEvent) -> Unit,
    state: ProjectUiState,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Project")
                },
                actions = {
                    IconButton(onClick = {
                        onEvent(
                            ProjectScreenEvent.OnInsertClick
                        )
                    }) {
                        Icon(imageVector = Icons.Default.Save, contentDescription = "save icon")
                    }
                }
            )
        }
    ) {
        when (state.isLoading) {
            true -> LoadingScreen(it)
            false -> ScreenContent(it, onEvent, state)
        }
    }
}

@Composable
private fun ScreenContent(
    paddingValues: PaddingValues,
    onEvent: (ProjectScreenEvent) -> Unit,
    state: ProjectUiState,
) {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
    ) {
        MyTextField(
            value = state.selectedProject?.projectName ?: "",
            placeholder = "Patince",
            error = state.projectNameError,
            label = "Project Name",
            onValueChanged = { text ->
                onEvent(
                    ProjectScreenEvent.OnProjectNameChange(text)
                )
            }
        )

        MyTextField(
            value = "${state.selectedProject?.numLocal ?: ""}",
            placeholder = "22",
            error = state.numLocalError,
            label = "Locality Count",
            keyType = KeyType.NUMBER,
            onValueChanged = { text ->
                onEvent(
                    ProjectScreenEvent.OnNumberLocalChange(text)
                )
            }
        )

        MyTextField(
            value = "${state.selectedProject?.numMice ?: ""}",
            placeholder = "33",
            error = state.numMiceError,
            label = "Mouse Count",
            keyType = KeyType.NUMBER,
            onValueChanged = { text ->
                onEvent(
                    ProjectScreenEvent.OnNumberMiceChange(text)
                )
            }
        )
    }
}