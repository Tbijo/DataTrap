package com.example.datatrap.sync.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.datatrap.core.presentation.components.MyScaffold

@Composable
fun SyncScreen(
    onEvent: String
) {
    val numOfMice = 12
    val loading = false

    MyScaffold(
        title = "Synchronize",
        onDrawerItemClick = {
            onEvent
        }
    ) {
        Column(
            modifier = Modifier.padding(it),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Row {
                Text(text = "Number of Mice to upload: $numOfMice")
            }

            Spacer(modifier = Modifier.height(395.dp))

            if(loading) {
                Column {
                    CircularProgressIndicator()
                    Text(text = "Uploading...")
                }
            }

            Spacer(modifier = Modifier.height(150.dp))

            Button(onClick = { /*TODO*/ }) {
                Text(text = "Synchronize")
            }
        }
    }
}