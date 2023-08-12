package com.example.datatrap.mouse.presentation.mouse_recapture_list.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun RecaptureListItem() {
    val id = 301
    val weight = 20
    val locality = "Topolcany"
    val dateTime = "12.12.2012"
    val age = "Juvenile"
    val sex = "Female"
    val specie = "AAG"
    val sexActive = false
    val lactating = false
    val gravidity = false

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
    ) {

        Text(text = "ID: $id")

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(text = "Weight: ${weight}g")
            Text(text = "Locality: $locality")
            Text(text = "DateTime: $dateTime")
        }

        Spacer(modifier = Modifier.width(32.dp))

        Column {
            Text(text = "Age: $age")
            Text(text = "Sex: $sex")
            Text(text = "Specie: $specie")
        }

        Spacer(modifier = Modifier.width(32.dp))

        Column {
            Text(text = "Sex. Active: ${if(sexActive) "Yes" else "No"}")
            Text(text = "Lactating: ${if(lactating) "Yes" else "No"}")
            Text(text = "Gravidity: ${if(gravidity) "Yes" else "No"}")
        }
    }
}