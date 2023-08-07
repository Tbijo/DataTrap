package com.example.datatrap.core.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp

@Composable
fun LabeledText(
    label: String,
    text: String,
) {
    Column {
        Text(text = label, fontSize = 18.sp)
        Text(text = text, fontSize = 20.sp)
    }
}