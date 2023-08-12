package com.example.datatrap.occasion.presentation.occasion_detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.datatrap.R
import com.example.datatrap.core.presentation.components.LabeledText

@Composable
fun OccasionDetailScreen() {
    Scaffold {
        Column(
            modifier = Modifier
                .padding(it)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(space = 16.dp, alignment = Alignment.Top)
        ) {

            Image(painter = painterResource(id = R.drawable.empty), contentDescription = "Occasion Image")

            Row {
                LabeledText(label = "Order of Occasion in Session", text = "22")
                LabeledText(label = "Locality", text = "Topolcany")
            }

            Row {
                LabeledText(label = "Method", text = "MethodName")
                LabeledText(label = "MethodType", text = "MethodTypeName")
            }

            Row {
                LabeledText(label = "TrapType", text = "TrapTypeName")
                LabeledText(label = "Environment Type", text = "Environment TypeName")
            }

            Row {
                LabeledText(label = "VegType", text = "VegTypeName")
                LabeledText(label = "Occasion Created DateTime", text = "11.12.2019 12:12:12")
            }

            Row {
                LabeledText(label = "Number of Traps", text = "11")
                LabeledText(label = "Number of Mice Caught", text = "22")
            }

            Row {
                LabeledText(label = "Temperature °C", text = "22")
                LabeledText(label = "Weather", text = "Rain")
            }

            Row {
                LabeledText(label = "Legitimation", text = "Marosko")
                LabeledText(label = "Caught something", text = "Yes")
            }

            Row {
                LabeledText(label = "Number of Species", text = "11")
                LabeledText(label = "Number of Errors", text = "22")
                LabeledText(label = "Number of Close", text = "33")
            }

            Row {
                LabeledText(label = "Number of Predator", text = "44")
                LabeledText(label = "Number of PVP", text = "55")
                LabeledText(label = "Number of Other", text = "66")
            }

            LabeledText(label = "Note", text = "Note .... note")
        }
    }
}