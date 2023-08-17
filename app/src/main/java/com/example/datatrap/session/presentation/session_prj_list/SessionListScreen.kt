package com.example.datatrap.session.presentation.session_prj_list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.datatrap.session.presentation.session_prj_list.components.SessionListItem

@Composable
fun SessionListScreen(
    onEvent: () -> Unit,
) {

    val projectName = ""
    val localityName = ""

    Scaffold {
        Column(modifier = Modifier.padding(it)) {
            Row {
                Text(text = "Project Name: $projectName")
                Text(text = "Locality Name: $localityName")
            }

            LazyColumn {
                item {
                    SessionListItem()
                }
            }
        }
    }
}