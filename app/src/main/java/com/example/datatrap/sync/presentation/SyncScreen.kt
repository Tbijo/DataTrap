package com.example.datatrap.sync.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.datatrap.core.presentation.LoadingScreen
import com.example.datatrap.core.presentation.components.NavigationScaffold

@Composable
fun SyncScreen(
    onEvent: () -> Unit,
    state: SyncUiState,
) {
    when(state.isLoading) {
        true -> LoadingScreen()
        false -> ScreenContent(
            onEvent = onEvent,
            state = state,
        )
    }
}

@Composable
private fun ScreenContent(
    onEvent: () -> Unit,
    state: SyncUiState,
) {
    val numOfMice = 12
    val loading = false

    NavigationScaffold(
        title = "Synchronize",
        errorState = state.error,
        onDrawerItemClick = {
            onEvent()
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