package com.example.datatrap.occasion.presentation.occasion_add_edit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.datatrap.core.presentation.components.MyTextField
import com.example.datatrap.core.presentation.components.ToggleButton

@Composable
fun OccasionScreen(
    onEvent: () -> Unit,
) {
    Scaffold {
        Column(
            modifier = Modifier.padding(it)
        ) {
            DropdownMenu(expanded = true, onDismissRequest = { /*TODO*/ }) {
                DropdownMenuItem(onClick = { /*TODO*/ }) {
                    Text(text = "Environment type")
                }
            }

            DropdownMenu(expanded = true, onDismissRequest = { /*TODO*/ }) {
                DropdownMenuItem(onClick = { /*TODO*/ }) {
                    Text(text = "Method*")
                }
            }

            DropdownMenu(expanded = true, onDismissRequest = { /*TODO*/ }) {
                DropdownMenuItem(onClick = { /*TODO*/ }) {
                    Text(text = "Method type*")
                }
            }

            DropdownMenu(expanded = true, onDismissRequest = { /*TODO*/ }) {
                DropdownMenuItem(onClick = { /*TODO*/ }) {
                    Text(text = "Trap type*")
                }
            }

            DropdownMenu(expanded = true, onDismissRequest = { /*TODO*/ }) {
                DropdownMenuItem(onClick = { /*TODO*/ }) {
                    Text(text = "Vegetation type")
                }
            }

            ToggleButton(text = "Got Caught?", isSelected = false) {
                
            }
            
            MyTextField(value = "123", placeholder = "Number of traps*", error = null, label = "Number of traps*",
                onValueChanged = {
                    // TODO
                }
            )

            MyTextField(value = "Rain", placeholder = "Weather", error = null, label = "Weather",
                onValueChanged = {
                    // TODO
                }
            )

            MyTextField(value = "Legit", placeholder = "Legitimation*", error = null, label = "Legitimation*",
                onValueChanged = {
                    // TODO
                }
            )

            MyTextField(value = "Note... note", placeholder = "Note", error = null, label = "Note",
                onValueChanged = {
                    // TODO
                }
            )
        }
    }
}