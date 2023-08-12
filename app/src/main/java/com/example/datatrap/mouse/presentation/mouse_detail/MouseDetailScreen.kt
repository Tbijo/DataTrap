package com.example.datatrap.mouse.presentation.mouse_detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.example.datatrap.R
import com.example.datatrap.core.presentation.components.LabeledText

@Composable
fun MouseDetailScreen(
    title: String = "Individual Code: ${args.mouseOccTuple.mouseCode}"
) {
    Scaffold(
        topBar = {
            Text(text = title)
        }
    ) {
        Column(modifier = Modifier.padding(it)) {

            Image(painter = painterResource(id = R.drawable.empty), contentDescription = "mouse image")

            Row {
                LabeledText(label = "Project", text = "Komjatice")
                LabeledText(label = "Specie Code", text = "AAG")
                LabeledText(label = "Species Full Name", text = "Agrelis Midisis")
            }

            Row {
                LabeledText(label = "Weight (g)", text = "38.0")
                LabeledText(label = "Age", text = "Adul")
                LabeledText(label = "Sex", text = "Male")
            }

            Row {
                LabeledText(label = "Gravidity", text = "No")
                LabeledText(label = "Lactating", text = "No")
                LabeledText(label = "Sex. Active", text = "No")
            }

            Row {
                LabeledText(label = "Body Length (mm)", text = "5.4")
                LabeledText(label = "Tail Length (mm)", text = "5.4")
                LabeledText(label = "Feet Length (mm)", text = "5.4")
                LabeledText(label = "Ear Length (mm)", text = "5.4")
            }

            Row {
                LabeledText(label = "Catch DateTime", text = "20.12.2021 12:50:33")
                LabeledText(label = "Legitimation", text = "Mister Perfect")
                LabeledText(label = "Note", text = "Note ... note.")
            }

            Row {
                LabeledText(label = "Testes Length", text = "9.6")
                LabeledText(label = "Testes Width", text = "9.6")
            }

            Row {
                LabeledText(label = "Embryo Right", text = "6")
                LabeledText(label = "Embryo Left", text = "4")
                LabeledText(label = "Embryo Diameter", text = "9.6")
            }

            Row {
                LabeledText(label = "MC Right", text = "10")
                LabeledText(label = "MC Left", text = "10")
                LabeledText(label = "MC", text = "Yes")
            }

            // History of previous captures
            LazyColumn {
                item {
                    Text(text = "Mouse Log")
                }
            }
        }
    }
}