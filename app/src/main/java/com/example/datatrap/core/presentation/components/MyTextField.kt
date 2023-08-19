package com.example.datatrap.core.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

enum class KeyType {
    NUMBER, DEFAULT
}

@Composable
fun MyTextField(
    value: String,
    placeholder: String,
    error: String?,
    label: String,
    onValueChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
    keyType: KeyType = KeyType.DEFAULT,
) {
    Column(modifier) {
        OutlinedTextField(
            value = value,
            placeholder = {
                Text(text = placeholder)
            },
            label = {
                Text(text = label)
            },
            isError = (error != null),
            keyboardOptions = when(keyType) {
                KeyType.NUMBER -> KeyboardOptions(keyboardType = KeyboardType.Number)
                KeyType.DEFAULT -> KeyboardOptions.Default
            },
            onValueChange = onValueChanged,
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.fillMaxWidth()
        )
        error?.let {
            Text(
                text = error,
                color = Color.Red
            )
        }
    }
}