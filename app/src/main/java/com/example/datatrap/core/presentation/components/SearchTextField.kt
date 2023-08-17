package com.example.datatrap.core.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle

@Composable
fun SearchTextField(
    text: String,
    hint: String,
    isHintVisible: Boolean = true,
    onValueChange: (String) -> Unit,
    textStyle: TextStyle = MaterialTheme.typography.body1,
    onFocusChange: (FocusState) -> Unit,
) {
    Box {
        OutlinedTextField(
            value = text,
            label = {
                if(isHintVisible) {
                    Text(text = hint, style = textStyle, color = Color.DarkGray)
                }
            },
            onValueChange = onValueChange,
            singleLine = true,
            textStyle = textStyle,
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged {
                    onFocusChange(it)
                }
        )
    }
}