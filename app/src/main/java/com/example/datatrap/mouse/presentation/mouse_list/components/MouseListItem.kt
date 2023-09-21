package com.example.datatrap.mouse.presentation.mouse_list.components

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
import androidx.compose.material.icons.filled.Update
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.datatrap.mouse.domain.model.MouseListData

@Composable
fun MouseListItem(
    mouse: MouseListData,
    onItemClick: () -> Unit,
    onUpdateClick: () -> Unit,
    onDeleteClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClick() },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column {
            Text(text = "Individual Code: ${mouse.mouseCode}")
            Text(text = "Species Code: ${mouse.specieCode}")
            Text(text = "Catch DateTime: ${mouse.mouseCaught}")
        }

        Spacer(modifier = Modifier.weight(1f))

        IconButton(onClick = onUpdateClick) {
            Icon(
                imageVector = Icons.Default.Update,
                contentDescription = "update button"
            )
        }

        IconButton(onClick = onDeleteClick) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "delete button"
            )
        }
    }
}