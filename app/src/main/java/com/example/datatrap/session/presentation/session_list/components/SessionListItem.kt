package com.example.datatrap.session.presentation.session_list.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Update
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.ZonedDateTime

@Composable
fun SessionListItem(
    sessionNumber: Int,
    numberOfOccasions: Int,
    dateTime: ZonedDateTime,
    onItemClick: () -> Unit,
    onUpdateClick: () -> Unit,
    onDeleteClick: () -> Unit,
) {

    Row {
        Column(
            modifier = Modifier.fillMaxWidth()
                .clickable { onItemClick() }
        ) {
            Row {
                Text(text = "$sessionNumber", fontSize = 20.sp)

                Spacer(modifier = Modifier.width(16.dp))

                Text(text = "Occation Count: $numberOfOccasions", fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.width(8.dp))

            Text(text = dateTime.toString(), fontSize = 18.sp)
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