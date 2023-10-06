package com.example.datatrap.camera.presentation

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.datatrap.core.presentation.LoadingScreen
import com.example.datatrap.core.presentation.components.MyImage
import com.example.datatrap.core.presentation.components.MyScaffold
import com.example.datatrap.core.presentation.components.MyTextField

@Composable
fun CameraScreen(
    onEvent: (CameraScreenEvent) -> Unit,
    state: CameraUiState,
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
    onEvent: (CameraScreenEvent) -> Unit,
    state: CameraUiState,
) {
    val takePhoto = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview(),
        onResult = {
            it?.let {
                onEvent(CameraScreenEvent.OnImageReceived(it))
            }
        },
    )

    MyScaffold(
        title = "Camera",
        errorState = state.error,
    ) {
        Column(modifier = Modifier.padding(it)) {

            MyImage(
                modifier = Modifier.size(370.dp),
                imgBitmap = state.bitmap,
                contentDescription = "${
                    state.mouseImageEntity?.let { 
                        "mouse image"
                    } ?: state.occasionImageEntity?.let {
                        "occasion image"
                    }
                }",
                onClick = {},
            )

            Spacer(modifier = Modifier.height(300.dp))

            Button(
                onClick = {
                    takePhoto.launch()
                },
            ) {
                Text(text = "Take Image")
            }

            Spacer(modifier = Modifier.height(28.dp))

            Text(text = state.imageStateText)

            Spacer(modifier = Modifier.height(32.dp))

            MyTextField(
                value = state.note ?: "", placeholder = "Note", error = null, label = "Note",
                onValueChanged = { text ->
                    onEvent(CameraScreenEvent.OnNoteTextChanged(text))
                }
            )

            Spacer(modifier = Modifier.height(57.dp))

            Button(
                onClick = {
                    onEvent(CameraScreenEvent.OnInsertClick)
                },
            ) {
                Text(text = "Save Image")
            }
        }
    }
}