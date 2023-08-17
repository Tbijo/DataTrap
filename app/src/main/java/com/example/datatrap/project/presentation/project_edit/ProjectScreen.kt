package com.example.datatrap.project.presentation.project_edit

import androidx.compose.foundation.layout.Column
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
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(it)
        ) {
            MyTextField(
                value = state.selectedProject?.projectName.toString(),
                placeholder = "Project Name",
                error = null,
                label = "Project Name",
                onValueChanged = {
                    // TODO
                }
            )

            MyTextField(
                value = state.selectedProject?.numLocal.toString(),
                placeholder = "Locality Count",
                error = null,
                label = "Locality Count",
                onValueChanged = {
                    // TODO
                }
            )

            MyTextField(
                value = state.selectedProject?.numMice.toString(),
                placeholder = "Mouse Count",
                error = null,
                label = "Mouse Count",
                onValueChanged = {
                    // TODO
                }
            )
        }
    }
}