package com.example.datatrap.specie.presentation.specie_image

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.datatrap.core.presentation.LoadingScreen
import com.example.datatrap.core.presentation.components.MyScaffold
import com.example.datatrap.core.presentation.components.MyTextField

@Composable
fun SpecieImageScreen(
    onEvent: (SpecieImageScreenEvent) -> Unit,
    state: SpecieImageUiState,
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
    onEvent: (SpecieImageScreenEvent) -> Unit,
    state: SpecieImageUiState,
) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            onEvent(
                SpecieImageScreenEvent.OnImageResult(uri = uri)
            )
        }
    }

    MyScaffold(
        title = "Specie Image",
        errorState = state.error,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            AsyncImage(
                model = state.imageUri,
                contentDescription = "specie image",
                modifier = Modifier.size(350.dp),
            )

            Spacer(modifier = Modifier.height(292.dp))

            Button(
                onClick = {
                     launcher.launch("image/*")
                }) {
                Text(text = "Get Image")
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(text = state.imageStateText)

            Spacer(modifier = Modifier.height(56.dp))

            MyTextField(value = state.note ?: "", placeholder = "Note...", error = null, label = "Note",
                onValueChanged = { text ->
                    onEvent(SpecieImageScreenEvent.OnNoteTextChanged(text))
                }
            )

            Spacer(modifier = Modifier.height(51.dp))

            Button(onClick = {
                onEvent(SpecieImageScreenEvent.OnInsertClick)
            }) {
                Text(text = "Save Image")
            }

        }
    }
}