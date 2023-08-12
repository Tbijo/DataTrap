package com.example.datatrap.session.presentation.session_prj_list.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SessionListItem() {

    val numberOfOccasions = 23

    Column {

        Row {
            Text(text = "Session number 123", fontSize = 20.sp)

            Spacer(modifier = Modifier.width(16.dp))

            Text(text = "Occation Count: $numberOfOccasions", fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.width(8.dp))

        Text(text = "12.12.2021 13:21:43", fontSize = 18.sp)
    }
}