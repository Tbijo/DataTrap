package com.example.datatrap.specie.presentation.specie_image

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.datatrap.R
import com.example.datatrap.core.presentation.components.MyTextField

@Composable
fun SpecieImageScreen() {
    Scaffold {
        Column(modifier = Modifier.padding(it)) {
            Image(
                painter = painterResource(id = R.drawable.empty),
                contentDescription = "mouse image",
                modifier = Modifier.size(350.dp),
            )

            Spacer(modifier = Modifier.height(292.dp))

            Button(onClick = { /*TODO*/ }) {
                Text(text = "Get Image")
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(text = "No Image")

            Spacer(modifier = Modifier.height(56.dp))

            MyTextField(value = "Note... note.", placeholder = "Note", error = null, label = "Note",
                onValueChanged = {
                    // TODO
                }
            )

            Spacer(modifier = Modifier.height(51.dp))

            Button(onClick = { /*TODO*/ }) {
                Text(text = "Save Image")
            }

        }
    }
}