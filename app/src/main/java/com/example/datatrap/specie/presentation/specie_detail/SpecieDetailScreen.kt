package com.example.datatrap.specie.presentation.specie_detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.datatrap.R
import com.example.datatrap.core.presentation.components.LabeledText

@Composable
fun SpecieDetailScreen() {
    Scaffold {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
        ) {
            Image(painter = painterResource(id = R.drawable.empty), contentDescription = "Specie Image")

            Spacer(modifier = Modifier.height(16.dp))

            Row {
                LabeledText(label = "Specie Code", text = "AAG")
                LabeledText(label = "Full Name", text = "Agrelus Gussu")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row {
                LabeledText(label = "Authority", text = "Author")
                LabeledText(label = "Synonymum", text = "Mouse Name")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row {
                LabeledText(label = "Authority", text = "Author")
                LabeledText(label = "Synonymum", text = "Mouse Name")
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}