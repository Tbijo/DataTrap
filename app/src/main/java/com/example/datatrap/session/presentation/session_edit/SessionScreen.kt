package com.example.datatrap.session.presentation.session_edit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.datatrap.core.presentation.components.LabeledText
import com.example.datatrap.core.presentation.components.MyTextField

@Composable
fun SessionScreen() {
    Scaffold {
        Column(modifier = Modifier.padding(it)) {

            MyTextField(value = "123", placeholder = "Session Number*", error = null, label = "Session Number*",
                onValueChanged = {
                // TODO
            })
            MyTextField(value = "1244", placeholder = "Occasion Count*", error = null, label = "Occasion Count*",
                onValueChanged = {
                // TODO
            })

            Spacer(modifier = Modifier.height(64.dp))

            TextButton(
                modifier = Modifier.align(Alignment.End),
                onClick = { /*TODO*/ },
            ) {
                Text(text = "Delete Association with Locality")
            }
        }
    }
}