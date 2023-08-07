package com.example.datatrap.login.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.RadioButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.datatrap.core.presentation.components.MyTextField
import com.example.datatrap.core.presentation.components.ToggleButton
import com.example.datatrap.core.util.EnumTeam

@Composable
fun LoginScreen() {

    Scaffold {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MyTextField(
                value = "name",
                placeholder = "User Name",
                error = null,
                label = "User Name",
                onValueChanged = {
                    // TODO
                }
            )

            MyTextField(
                value = "password",
                placeholder = "User Name",
                error = null,
                label = "User Name",
                onValueChanged = {
                    // TODO
                }
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                ToggleButton(text = EnumTeam.EVEN_TEAM.label, isSelected = false, onClick = {
                    // TODO
                },)
                ToggleButton(text = EnumTeam.ODD_TEAM.label, isSelected = false, onClick = {
                    // TODO
                },)
                ToggleButton(text = EnumTeam.SINGLE.label, isSelected = false, onClick = {
                    // TODO
                },)
            }

            TextButton(onClick = {
                // TODO
            }) {
                Text(text = "Log In")
            }

        }
    }
}