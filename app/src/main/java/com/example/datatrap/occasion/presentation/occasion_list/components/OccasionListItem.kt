package com.example.datatrap.occasion.presentation.occasion_list.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun OccasionListItem() {
    val trapCount = 5
    val miceCount = 2

    Row {
        Column {
            Text(text = "Occasion number: 5")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "20.12.2012")
        }

        Spacer(modifier = Modifier.height(14.dp))

        Column {
            Text(text = "Trap Count: $trapCount")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Mice Count: $miceCount")
        }
    }
}