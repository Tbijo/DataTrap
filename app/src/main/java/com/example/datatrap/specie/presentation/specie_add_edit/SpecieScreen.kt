package com.example.datatrap.specie.presentation.specie_add_edit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.datatrap.core.presentation.LoadingScreen
import com.example.datatrap.core.presentation.components.MyScaffold
import com.example.datatrap.core.presentation.components.MyTextField
import com.example.datatrap.core.presentation.components.ToggleButton

@Composable
fun SpecieScreen(
    onEvent: (SpecieScreenEvent) -> Unit,
    state: SpecieUiState,
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
    onEvent: (SpecieScreenEvent) -> Unit,
    state: SpecieUiState,
) {
    val scrollState = rememberScrollState()

    MyScaffold(
        title = "Specie",
        errorState = state.error,
        actions = {
            IconButton(onClick = { onEvent(SpecieScreenEvent.OnInsertClick) }) {
                Icon(imageVector = Icons.Default.Save, contentDescription = "save icon")
            }
            IconButton(onClick = { onEvent(SpecieScreenEvent.OnCameraClick) }) {
                Icon(imageVector = Icons.Default.Camera, contentDescription = "cam icon")
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .verticalScroll(scrollState),
        ) {
            MyTextField(value = state.specieCode, placeholder = "AAG", error = state.specieCodeError, label = "Specie Code",
                onValueChanged = { text ->
                    onEvent(SpecieScreenEvent.OnSpecieCodeTextChanged(text))
                })
            MyTextField(value = state.fullName, placeholder = "Mouses Polus", error = state.fullNameError, label = "Full Name",
                onValueChanged = { text ->
                    onEvent(SpecieScreenEvent.OnFullNameTextChanged(text))
                })
            MyTextField(value = state.synonym, placeholder = "Musaya Isrelis", error = state.synonymError, label = "Synonym",
                onValueChanged = { text ->
                    onEvent(SpecieScreenEvent.OnSynonymTextChanged(text))
                })
            MyTextField(value = state.authority, placeholder = "Julko", error = state.authorityError, label = "Authority",
                onValueChanged = { text ->
                    onEvent(SpecieScreenEvent.OnAuthorityTextChanged(text))
                })
            MyTextField(value = state.description, placeholder = "A mouse.", error = state.descriptionError, label = "Description",
                onValueChanged = { text ->
                    onEvent(SpecieScreenEvent.OnDescriptionTextChanged(text))
                })

            Spacer(modifier = Modifier.height(24.dp))

            ToggleButton(text = "Is it a small mammal?", isSelected = state.isSmall) {
                onEvent(SpecieScreenEvent.OnIsSmallClick)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Number of fingers?")
            Row {
                ToggleButton(text = "4", isSelected = state.numOfFingers == 4) {
                    onEvent(SpecieScreenEvent.OnNumFingersClick(4))
                }
                ToggleButton(text = "5", isSelected = state.numOfFingers == 5) {
                    onEvent(SpecieScreenEvent.OnNumFingersClick(5))
                }
                ToggleButton(text = "N/A", isSelected = state.numOfFingers == null) {
                    onEvent(SpecieScreenEvent.OnNumFingersClick(null))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row {
                MyTextField(value = state.minWeight, placeholder = "3.7", error = null, label = "Minimal Weight",
                    onValueChanged = { text ->
                        onEvent(SpecieScreenEvent.OnMinWeightTextChanged(text))
                    })
                MyTextField(value = state.maxWeight, placeholder = "3.7", error = null, label = "Maximal Weight",
                    onValueChanged = { text ->
                        onEvent(SpecieScreenEvent.OnMaxWeightTextChanged(text))
                    })
            }

            Row {
                MyTextField(value = state.bodyLength, placeholder = "3.7", error = null, label = "Body Length",
                    onValueChanged = { text ->
                        onEvent(SpecieScreenEvent.OnBodyLenTextChanged(text))
                    })
                MyTextField(value = state.tailLength, placeholder = "3.7", error = null, label = "Tail Length",
                    onValueChanged = { text ->
                        onEvent(SpecieScreenEvent.OnTailLenTextChanged(text))
                    })
            }

            Row {
                MyTextField(value = state.minFeetLength, placeholder = "3.7", error = null, label = "Minimal Feet Length",
                    onValueChanged = { text ->
                        onEvent(SpecieScreenEvent.OnMinFeetLenTextChanged(text))
                    })
                MyTextField(value = state.maxFeetLength, placeholder = "3.7", error = null, label = "Maximal Feet Length",
                    onValueChanged = { text ->
                        onEvent(SpecieScreenEvent.OnMaxFeetLenTextChanged(text))
                    })
            }

            MyTextField(value = state.note, placeholder = "Note..", error = null, label = "Note",
                onValueChanged = { text ->
                    onEvent(SpecieScreenEvent.OnNoteTextChanged(text))
                })
        }
    }
}