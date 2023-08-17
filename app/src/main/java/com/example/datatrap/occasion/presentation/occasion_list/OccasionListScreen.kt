package com.example.datatrap.occasion.presentation.occasion_list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.datatrap.occasion.presentation.occasion_list.components.OccasionListItem

@Composable
fun OccasionListScreen(
    onEvent: () -> Unit,
) {
    val projectName = "ProjName"
    val localityName = "ProjName"
    val sessionNum = "ProjName"

    Scaffold {
        Column(modifier = Modifier.padding(it)) {

            Row {
                Text(text = "Project Name: $projectName")
                Text(text = "Locality Name: $localityName")
                Text(text = "Session Num: $sessionNum")
            }

            LazyColumn {
                item {
                    OccasionListItem()
                }
            }
        }
    }
}