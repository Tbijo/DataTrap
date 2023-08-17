package com.example.datatrap.project.presentation.project_list.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.example.datatrap.project.data.ProjectEntity

@Composable
fun ProjectListItem(
    projectEntity: ProjectEntity,
    onListItemClick: () -> Unit,
    onDeleteClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onListItemClick()
            }
    ) {
        Column {
            Text(text = projectEntity.projectName, fontSize = 20.sp)
            Text(text = projectEntity.projectDateTimeCreated.toString(), fontSize = 18.sp)
        }
        Column {
            Text(text = "Locality Count: ${projectEntity.numLocal}", fontSize = 18.sp)
            Text(text = "Mouse Count: ${projectEntity.numMice}", fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.weight(1f))

        IconButton(onClick = onDeleteClick) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "delete button"
            )
        }
    }
}