package com.example.datatrap.project.presentation.project_list

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.datatrap.core.presentation.components.MyScaffold
import com.example.datatrap.project.presentation.project_list.components.ProjectListItem

@Composable
fun ProjectListScreen() {
    MyScaffold(
        title = "Projects",
        floatingActionButton = {
            FloatingActionButton(onClick = { /*TODO*/ }) {
                Icons.Default.Add
            }
        }
    ) {
        LazyColumn(modifier = Modifier.padding(it)) {
            item {
                ProjectListItem()
            }
        }
    }
}