package com.example.datatrap.project.presentation.project_list.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp

@Composable
fun ProjectListItem() {
    val localityCount = 12
    val mouseCount = 12

    Row {
        Column {
            Text(text = "Project Name", fontSize = 20.sp)
            Text(text = "12.12.2021 13:21:32", fontSize = 18.sp)
        }
        Column {
            Text(text = "Locality Count: $localityCount", fontSize = 18.sp)
            Text(text = "Mouse Count: $mouseCount", fontSize = 18.sp)
        }
    }
}