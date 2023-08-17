package com.example.datatrap.specie.presentation.specie_add_edit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.datatrap.core.presentation.components.MyTextField
import com.example.datatrap.core.presentation.components.ToggleButton

@Composable
fun SpecieScreen(
    onEvent: () -> Unit,
) {
    val scrollState = rememberScrollState()

    Scaffold {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .verticalScroll(scrollState),
        ) {
            MyTextField(value = "AAG", placeholder = "Specie Code", error = null, label = "Specie Code", onValueChanged = {
                // TODO
            })
            MyTextField(value = "Agrelus", placeholder = "Full Name", error = null, label = "Full Name", onValueChanged = {
                // TODO
            })
            MyTextField(value = "Agrelis", placeholder = "Synonym", error = null, label = "Synonym", onValueChanged = {
                // TODO
            })
            MyTextField(value = "...", placeholder = "Authority", error = null, label = "Authority", onValueChanged = {
                // TODO
            })
            MyTextField(value = "...", placeholder = "Description", error = null, label = "Description", onValueChanged = {
                // TODO
            })

            Spacer(modifier = Modifier.height(24.dp))
            ToggleButton(text = "Is it a small mammal?", isSelected = false) {
                // TODO
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Number of fingers?")
            Row {
                ToggleButton(text = "4", isSelected = false) {
                    // TODO
                }
                ToggleButton(text = "5", isSelected = false) {
                    // TODO
                }
                ToggleButton(text = "N/A", isSelected = false) {
                    // TODO
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            Row {
                MyTextField(value = "...", placeholder = "Minimal Weight", error = null, label = "Minimal Weight", onValueChanged = {
                    // TODO
                })
                MyTextField(value = "...", placeholder = "Maximal Weight", error = null, label = "Maximal Weight", onValueChanged = {
                    // TODO
                })
            }

            Row {
                MyTextField(value = "...", placeholder = "Body Length", error = null, label = "Body Length", onValueChanged = {
                    // TODO
                })
                MyTextField(value = "...", placeholder = "Tail Length", error = null, label = "Tail Length", onValueChanged = {
                    // TODO
                })
            }

            Row {
                MyTextField(value = "...", placeholder = "Minimal Feet Length", error = null, label = "Minimal Feet Length", onValueChanged = {
                    // TODO
                })
                MyTextField(value = "...", placeholder = "Maximal Feet Length", error = null, label = "Maximal Feet Length", onValueChanged = {
                    // TODO
                })
            }

            MyTextField(value = "...", placeholder = "Note", error = null, label = "Note", onValueChanged = {
                // TODO
            })

        }
    }
}