package com.example.datatrap.mouse.presentation.mouse_list.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Composable
fun MouseListItem() {
    val indiCode = 112
    val specieCode = "AAG"
    val dateTime = "2021.12.22 11:31"

    Column {
        Text(text = "Individual Code: $indiCode")
        Text(text = "Species Code: $specieCode")
        Text(text = "Catch DateTime: $dateTime")
    }
}