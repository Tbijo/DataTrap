package com.example.datatrap.occasion.presentation.occasion_list.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Details
import androidx.compose.material.icons.filled.Update
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.datatrap.occasion.data.occasion.OccasionEntity

@Composable
fun OccasionListItem(
    occasionEntity: OccasionEntity,
    onItemClick: () -> Unit,
    onDetailClick: () -> Unit,
    onUpdateClick: () -> Unit,
    onDeleteClick: () -> Unit,
) {
    Row(modifier = Modifier.fillMaxWidth().clickable {
        onItemClick()
    }) {
        Column {
            Text(text = "Occasion number: ${occasionEntity.occasion}")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "${occasionEntity.occasionDateTimeCreated}")
        }

        Spacer(modifier = Modifier.height(14.dp))

        Column {
            Text(text = "Trap Count: ${occasionEntity.numTraps}")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Mice Count: ${occasionEntity.numMice}")
        }

        Spacer(modifier = Modifier.weight(1f))

        IconButton(onClick = onDetailClick) {
            Icon(
                imageVector = Icons.Default.Details,
                contentDescription = "detail button"
            )
        }

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