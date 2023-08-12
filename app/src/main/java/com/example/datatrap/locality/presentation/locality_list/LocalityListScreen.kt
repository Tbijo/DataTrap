package com.example.datatrap.locality.presentation.locality_list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.datatrap.locality.presentation.locality_list.components.LocalityListItem

@Composable
fun LocalityListScreen() {

    val projectName = "ProjectTest"

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { /*TODO*/ }) {
                Icons.Default.Add
            }
        }
    ) {
        Column(
            modifier = Modifier.padding(it)
        ) {
            Text(text = "Project Name: $projectName")

            LazyColumn {
                item {
                    LocalityListItem()
                }
            }
        }
    }
}