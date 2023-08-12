package com.example.datatrap.mouse.presentation.mouse_add_multi.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Composable
fun MouseMultiItem() {
    Row {
        DropdownMenu(expanded = true, onDismissRequest = { /*TODO*/ }) {
            DropdownMenuItem(onClick = { /*TODO*/ }) {
                Text(text = "TrapID*")
            }
        }

        DropdownMenu(expanded = true, onDismissRequest = { /*TODO*/ }) {
            DropdownMenuItem(onClick = { /*TODO*/ }) {
                Text(text = "Specie*")
            }
        }
    }
}