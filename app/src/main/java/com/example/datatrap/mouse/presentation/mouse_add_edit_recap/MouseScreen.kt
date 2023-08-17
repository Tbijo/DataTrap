package com.example.datatrap.mouse.presentation.mouse_add_edit_recap

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.datatrap.core.presentation.components.MyTextField
import com.example.datatrap.core.presentation.components.ToggleButton

@Composable
fun MouseScreen(
    onEvent: () -> Unit,
) {
    Scaffold {
        Column(
            modifier = Modifier
                .padding(it)
                .verticalScroll(rememberScrollState())
        ) {
            Row {
                MyTextField(value = "301", placeholder = "Mouse Code", error = null, label = "Mouse Code",
                    onValueChanged = {
                        // TODO
                    }
                )
                // Disable on Update
                Button(onClick = { /*TODO*/ }, enabled = true) {
                    Text(text = "Generate Code")
                }
            }

            Row {
                DropdownMenu(expanded = true, onDismissRequest = { /*TODO*/ }) {
                    DropdownMenuItem(onClick = { /*TODO*/ }) {
                        Text(text = "Specie*")
                    }
                }
                DropdownMenu(expanded = true, onDismissRequest = { /*TODO*/ }) {
                    DropdownMenuItem(onClick = { /*TODO*/ }) {
                        Text(text = "TrapID")
                    }
                }
                DropdownMenu(expanded = true, onDismissRequest = { /*TODO*/ }) {
                    DropdownMenuItem(onClick = { /*TODO*/ }) {
                        Text(text = "Protocol")
                    }
                }
            }

            Row {
                // Sex
                ToggleButton(text = "Male", isSelected = false) {
                    // TODO
                }
                ToggleButton(text = "Female", isSelected = false) {
                    // TODO
                }
                ToggleButton(text = "N/A", isSelected = false) {
                    // TODO
                }
            }

            Row {
                // Age
                ToggleButton(text = "Juvenile", isSelected = false) {
                    // TODO
                }
                ToggleButton(text = "Subadult", isSelected = false) {
                    // TODO
                }
                ToggleButton(text = "Adult", isSelected = false) {
                    // TODO
                }
                ToggleButton(text = "N/A", isSelected = false) {
                    // TODO
                }
            }

            Row {
                // CaptureID
                ToggleButton(text = "Died", isSelected = false) {
                    // TODO
                }
                ToggleButton(text = "Captured", isSelected = false) {
                    // TODO
                }
                ToggleButton(text = "Released", isSelected = false) {
                    // TODO
                }
                ToggleButton(text = "Escaped", isSelected = false) {
                    // TODO
                }
                ToggleButton(text = "N/A", isSelected = false) {
                    // TODO
                }
            }

            Row {
                // Sexual Activity / checkbox
                ToggleButton(text = "Sex. Active", isSelected = false) {
                    // TODO
                }
                ToggleButton(text = "Lactating", isSelected = false) {
                    // TODO
                }
                ToggleButton(text = "Gravidity", isSelected = false) {
                    // TODO
                }
            }
            
            Row {
                MyTextField(value = "11.3", placeholder = "Body", error = null, label = "Body",
                    onValueChanged = {
                        // TODO
                    }
                )
                MyTextField(value = "11.3", placeholder = "Tail", error = null, label = "Tail",
                    onValueChanged = {
                        // TODO
                    }
                )
                MyTextField(value = "11.3", placeholder = "Feet", error = null, label = "Feet",
                    onValueChanged = {
                        // TODO
                    }
                )
                MyTextField(value = "11.3", placeholder = "Ear", error = null, label = "Ear",
                    onValueChanged = {
                        // TODO
                    }
                )
            }

            Row {
                MyTextField(value = "11.3", placeholder = "Weight", error = null, label = "Weight",
                    onValueChanged = {
                        // TODO
                    }
                )
                MyTextField(value = "11.3", placeholder = "Testes Length", error = null, label = "Testes Length",
                    onValueChanged = {
                        // TODO
                    }
                )
                MyTextField(value = "11.3", placeholder = "Testes Width", error = null, label = "Testes Width",
                    onValueChanged = {
                        // TODO
                    }
                )
            }

            Row {
                MyTextField(value = "11", placeholder = "Right Embryo", error = null, label = "Right Embryo",
                    onValueChanged = {
                        // TODO
                    }
                )
                MyTextField(value = "11", placeholder = "Left Embryo", error = null, label = "Left Embryo",
                    onValueChanged = {
                        // TODO
                    }
                )
                MyTextField(value = "11.3", placeholder = "Embryo Diameter", error = null, label = "Embryo Diameter",
                    onValueChanged = {
                        // TODO
                    }
                )
            }

            Row {
                MyTextField(value = "11", placeholder = "MC Right", error = null, label = "MC Right",
                    onValueChanged = {
                        // TODO
                    }
                )
                MyTextField(value = "11", placeholder = "MC Left", error = null, label = "MC Left",
                    onValueChanged = {
                        // TODO
                    }
                )
                ToggleButton(text = "MC", isSelected = false) {
                    // TODO
                }
            }

            MyTextField(value = "Note... note", placeholder = "Note", error = null, label = "Note",
                onValueChanged = {
                    // TODO
                }
            )
        }
    }
}