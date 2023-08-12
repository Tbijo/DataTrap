package com.example.datatrap.camera.presentation

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
fun CameraScreen() {
    Scaffold {
        Column(modifier = Modifier.padding(it)) {

            Image(
                modifier = Modifier.size(370.dp),
                painter = painterResource(id = R.drawable.empty),
                contentDescription = "mouse image"
            )

            Spacer(modifier = Modifier.height(300.dp))

            Button(onClick = { /*TODO*/ }) {
                Text(text = "Take Image")
            }

            Spacer(modifier = Modifier.height(28.dp))

            Text(text = "No Image")

            Spacer(modifier = Modifier.height(32.dp))

            MyTextField(value = "Note ... note", placeholder = "Note", error = null, label = "Note",
                onValueChanged = {
                    // TODO
                }
            )

            Spacer(modifier = Modifier.height(57.dp))

            Button(onClick = { /*TODO*/ }) {
                Text(text = "Save Image")
            }
        }
    }
}