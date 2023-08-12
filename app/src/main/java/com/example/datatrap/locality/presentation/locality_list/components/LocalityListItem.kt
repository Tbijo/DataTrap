package com.example.datatrap.locality.presentation.locality_list.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp

@Composable
fun LocalityListItem() {

    val sessionCount = 12

    Row {
        Column {
            Text(text = "Locality Name: Patince", fontSize = 20.sp)
            Text(text = "12.12.2021", fontSize = 18.sp)
        }

        Text(text = "Session Count: $sessionCount", fontSize = 18.sp)
    }
}