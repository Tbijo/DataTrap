package com.example.datatrap.about

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.datatrap.core.presentation.components.MyScaffold

@Composable
fun AboutScreen(
    onEvent: () -> Unit,
) {
    MyScaffold(
        title = "About",
        onDrawerItemClick = {
            onEvent
        }
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(it),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = "About Screen")
        }
    }
}