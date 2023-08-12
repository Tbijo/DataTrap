package com.example.datatrap.locality.presentation.locality_add_edit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.datatrap.core.presentation.components.MyTextField

@Composable
fun LocalityScreen() {
    Scaffold {
        Column(
            modifier = Modifier.padding(it)
        ) {
            MyTextField(value = "Topolcany", placeholder = "Topolcany", error = null, label = "Locality Name*",
                onValueChanged = {
                    // TODO
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            MyTextField(value = "12", placeholder = "12", error = null, label = "Session Count*",
                onValueChanged = {
                    // TODO
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row {
                MyTextField(value = "12.12", placeholder = "12.12", error = null, label = "Latitude A",
                    onValueChanged = {
                        // TODO
                    }
                )

                MyTextField(value = "12.11", placeholder = "12.11", error = null, label = "Longnitude A",
                    onValueChanged = {
                        // TODO
                    }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(onClick = { /*TODO*/ }) {
                Text(text = "Get Coordinates A")
            }

            Spacer(modifier = Modifier.height(32.dp))

            Row {
                MyTextField(value = "12.13", placeholder = "12.12", error = null, label = "Latitude B",
                    onValueChanged = {
                        // TODO
                    }
                )

                MyTextField(value = "12.13", placeholder = "12.11", error = null, label = "Longnitude B",
                    onValueChanged = {
                        // TODO
                    }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(onClick = { /*TODO*/ }) {
                Text(text = "Get Coordinates B")
            }

            Spacer(modifier = Modifier.height(32.dp))

            MyTextField(value = "Note... note", placeholder = "Note... note", error = null, label = "Note",
                onValueChanged = {
                    // TODO
                }
            )
        }
    }
}