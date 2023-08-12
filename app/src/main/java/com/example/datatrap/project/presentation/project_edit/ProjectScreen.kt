package com.example.datatrap.project.presentation.project_edit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.datatrap.core.presentation.components.MyTextField

@Composable
fun ProjectScreen() {
    Scaffold {
        Column(modifier = Modifier.padding(it)) {
            MyTextField(value = "Project Name", placeholder = "Project Name", error = null, label = "Project Name",
                onValueChanged = {
                    // TODO
                }
            )

            MyTextField(value = "Locality Count", placeholder = "Locality Count", error = null, label = "Locality Count",
                onValueChanged = {
                    // TODO
                }
            )

            MyTextField(value = "Mouse Count", placeholder = "Mouse Count", error = null, label = "Mouse Count",
                onValueChanged = {
                    // TODO
                }
            )
        }
    }
}