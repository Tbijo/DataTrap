package com.example.datatrap.mouse.presentation.mouse_recapture_list.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.datatrap.mouse.domain.model.MouseRecapList

@Composable
fun RecaptureListItem(
    mouse: MouseRecapList,
    onItemClick: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth()
            .clickable {
                onItemClick()
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
    ) {

        Text(text = "ID: ${mouse.code}")

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(text = "Weight: ${mouse.weight}g")
            Text(text = "Locality: ${mouse.localityName}")
            Text(text = "DateTime: ${mouse.mouseCaught}")
        }

        Spacer(modifier = Modifier.width(32.dp))

        Column {
            Text(text = "Age: ${mouse.age}")
            Text(text = "Sex: ${mouse.sex}")
            Text(text = "Specie: ${mouse.specieCode}")
        }

        Spacer(modifier = Modifier.width(32.dp))

        Column {
            Text(text = "Sex. Active: ${if(mouse.sexActive == true) "Yes" else "No"}")
            Text(text = "Lactating: ${if(mouse.lactating == true) "Yes" else "No"}")
            Text(text = "Gravidity: ${if(mouse.gravidity == true) "Yes" else "No"}")
        }
    }
}