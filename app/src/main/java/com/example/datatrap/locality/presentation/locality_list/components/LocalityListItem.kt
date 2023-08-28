package com.example.datatrap.locality.presentation.locality_list.components

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.example.datatrap.locality.data.LocalityEntity

@Composable
fun LocalityListItem(
    localityEntity: LocalityEntity,
    onItemClick: () -> Unit,
    onUpdateClick: () -> Unit,
    onDeleteClick: () -> Unit,
) {
    Row(modifier = Modifier.fillMaxWidth().clickable {
        onItemClick()
    }) {
        Column {
            Text(text = localityEntity.localityName, fontSize = 20.sp)
            Text(text = "${localityEntity.localityDateTimeCreated}", fontSize = 18.sp)
        }

        Text(text = "Session Count: ${localityEntity.numSessions}", fontSize = 18.sp)

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