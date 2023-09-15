package com.example.datatrap.login.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.datatrap.core.presentation.LoadingScreen
import com.example.datatrap.core.presentation.components.MyScaffold
import com.example.datatrap.core.presentation.components.MyTextField
import com.example.datatrap.core.presentation.components.ToggleButton
import com.example.datatrap.core.util.ScienceTeam

@Composable
fun LoginScreen(
    onEvent: (LoginScreenEvent) -> Unit,
    state: LoginUiState,
) {
    when(state.isLoading) {
        true -> LoadingScreen()
        false -> ScreenContent(
            onEvent = onEvent,
            state = state,
        )
    }
}

@Composable
private fun ScreenContent(
    onEvent: (LoginScreenEvent) -> Unit,
    state: LoginUiState,
) {
    MyScaffold(
        title = "Log In",
        errorState = state.error
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MyTextField(
                value = state.userName,
                placeholder = "Palo",
                error = state.userNameError,
                label = "User Name",
                onValueChanged = { text ->
                    onEvent(
                        LoginScreenEvent.OnUserNameChanged(text)
                    )
                }
            )

            MyTextField(
                value = state.password,
                placeholder = "pali123",
                error = state.passwordError,
                label = "Password",
                onValueChanged = { text ->
                    onEvent(
                        LoginScreenEvent.OnPasswordChanged(text)
                    )
                }
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                ToggleButton(text = ScienceTeam.EVEN_TEAM.label, isSelected = state.selectedTeam == ScienceTeam.EVEN_TEAM,
                    onClick = {
                        onEvent(
                            LoginScreenEvent.OnSelectTeam(ScienceTeam.EVEN_TEAM)
                        )
                    },)
                ToggleButton(text = ScienceTeam.ODD_TEAM.label, isSelected = state.selectedTeam == ScienceTeam.ODD_TEAM,
                    onClick = {
                        onEvent(
                            LoginScreenEvent.OnSelectTeam(ScienceTeam.ODD_TEAM)
                        )
                    },)
                ToggleButton(text = ScienceTeam.SINGLE.label, isSelected = state.selectedTeam == ScienceTeam.SINGLE,
                    onClick = {
                        onEvent(
                            LoginScreenEvent.OnSelectTeam(ScienceTeam.SINGLE)
                        )
                    },)
            }

            TextButton(onClick = {
                onEvent(
                    LoginScreenEvent.LogIn
                )
            }) {
                Text(text = "Log In")
            }

        }
    }
}