package com.example.datatrap.occasion.presentation.occasion_add_edit

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.datatrap.core.presentation.LoadingScreen
import com.example.datatrap.core.presentation.components.MyScaffold
import com.example.datatrap.core.presentation.components.MyTextField
import com.example.datatrap.core.presentation.components.ToggleButton

@Composable
fun OccasionScreen(
    onEvent: (OccasionScreenEvent) -> Unit,
    state: OccasionUiState,
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
    onEvent: (OccasionScreenEvent) -> Unit,
    state: OccasionUiState,
) {
    MyScaffold(
        title = "Occasion",
        errorState = state.error,
        actions = {
            IconButton(onClick = {
                onEvent(
                    OccasionScreenEvent.OnInsertClick
                )
            }) {
                Icon(imageVector = Icons.Default.Save, contentDescription = "save icon")
            }
            IconButton(onClick = {
                onEvent(
                    OccasionScreenEvent.OnCameraClick
                )
            }) {
                Icon(imageVector = Icons.Default.CameraAlt, contentDescription = "cam icon")
            }
            IconButton(onClick = {
                onEvent(
                    OccasionScreenEvent.OnCloudClick
                )
            }) {
                Icon(imageVector = Icons.Default.Cloud, contentDescription = "cloud icon")
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            DropdownMenu(
                modifier = Modifier.clickable { onEvent(OccasionScreenEvent.OnEnvTypeDropDownClick) },
                expanded = state.isEnvTypeExpanded,
                onDismissRequest = { onEvent(OccasionScreenEvent.OnEnvTypeDropDownDismiss) },
            ) {
                state.occasionEntity?.let { occasion ->
                    val envType = state.envTypeList.find { envType -> envType.envTypeId == occasion.envTypeID }?.envTypeName
                    DropdownMenuItem(onClick = {}, enabled = false) {
                        Text(text = "$envType")
                    }
                }
                DropdownMenuItem(onClick = {}, enabled = false) {
                    Text(text = "Environment type")
                }
                state.envTypeList.forEach { envType ->
                    DropdownMenuItem(onClick = { onEvent(OccasionScreenEvent.OnSelectEnvType(envType)) }) {
                        Text(text = envType.envTypeName)
                    }
                }
            }

            DropdownMenu(
                modifier = Modifier.clickable { onEvent(OccasionScreenEvent.OnMethodDropDownClick) },
                expanded = state.isMethodExpanded,
                onDismissRequest = { onEvent(OccasionScreenEvent.OnMethodDropDownDismiss) }
            ) {
                state.occasionEntity?.let { occasion ->
                    val methodName = state.methodList.find { method ->
                        method.methodId == occasion.methodID
                    }?.methodName
                    DropdownMenuItem(onClick = {}, enabled = false) {
                        Text(text = "$methodName")
                    }
                }
                DropdownMenuItem(onClick = {}, enabled = false) {
                    Text(text = "Method*")
                }
                state.methodList.forEach { method ->
                    DropdownMenuItem(onClick = { onEvent(OccasionScreenEvent.OnSelectMethod(method)) }) {
                        Text(text = method.methodName)
                    }
                }
            }

            DropdownMenu(
                modifier = Modifier.clickable { onEvent(OccasionScreenEvent.OnMethodTypeDropDownClick) },
                expanded = state.isMethodTypeExpanded,
                onDismissRequest = { onEvent(OccasionScreenEvent.OnMethodTypeDropDownDismiss) }
            ) {
                state.occasionEntity?.let { occasion ->
                    val metTypeName = state.methodTypeList.find { methType ->
                        methType.methodTypeId == occasion.methodTypeID
                    }?.methodTypeName
                    DropdownMenuItem(onClick = {}, enabled = false) {
                        Text(text = "$metTypeName")
                    }
                }
                DropdownMenuItem(onClick = {}, enabled = false) {
                    Text(text = "Method type*")
                }
                state.methodTypeList.forEach { methodType ->
                    DropdownMenuItem(onClick = { onEvent(OccasionScreenEvent.OnSelectMethodType(methodType)) }) {
                        Text(text = methodType.methodTypeName)
                    }
                }
            }

            DropdownMenu(
                modifier = Modifier.clickable { onEvent(OccasionScreenEvent.OnTrapTypeDropDownClick) },
                expanded = state.isTrapTypeExpanded,
                onDismissRequest = { onEvent(OccasionScreenEvent.OnTrapTypeDropDownDismiss) }
            ) {
                state.occasionEntity?.let { occasion ->
                    val trapTypeName = state.trapTypeList.find { trapType ->
                        trapType.trapTypeId == occasion.trapTypeID
                    }?.trapTypeName
                    DropdownMenuItem(onClick = {}, enabled = false) {
                        Text(text = "$trapTypeName")
                    }
                }
                DropdownMenuItem(onClick = {}, enabled = false) {
                    Text(text = "Trap type*")
                }
                state.trapTypeList.forEach { trapType ->
                    DropdownMenuItem(onClick = { onEvent(OccasionScreenEvent.OnSelectTrapType(trapType)) }) {
                        Text(text = trapType.trapTypeName)
                    }
                }
            }

            DropdownMenu(
                modifier = Modifier.clickable { onEvent(OccasionScreenEvent.OnVegTypeDropDownClick) },
                expanded = state.isVegTypeExpanded,
                onDismissRequest = { onEvent(OccasionScreenEvent.OnVegTypeDropDownDismiss) }
            ) {
                state.occasionEntity?.let { occasion ->
                    val vegTypeName = state.vegTypeList.find { vegType ->
                        vegType.vegetTypeId == occasion.vegetTypeID
                    }?.vegetTypeName
                    DropdownMenuItem(onClick = {}, enabled = false) {
                        Text(text = "$vegTypeName")
                    }
                }
                DropdownMenuItem(onClick = {}, enabled = false) {
                    Text(text = "Vegetation type")
                }
                state.vegTypeList.forEach { vegType ->
                    DropdownMenuItem(onClick = { onEvent(OccasionScreenEvent.OnSelectVegType(vegType)) }) {
                        Text(text = vegType.vegetTypeName)
                    }
                }
            }

            ToggleButton(text = "Got Caught?", isSelected = state.gotCaught) {
                onEvent(OccasionScreenEvent.OnGotCaughtClick)
            }

            MyTextField(
                value = state.numberOfTrapsText,
                placeholder = "11",
                error = state.numberOfTrapsError,
                label = "Number of traps*",
                onValueChanged = { text ->
                    onEvent(OccasionScreenEvent.OnNumberOfTrapsChanged(text))
                }
            )

            MyTextField(
                value = state.numberOfMiceText,
                placeholder = "22",
                error = state.numberOfMiceError,
                label = "Number of mice",
                onValueChanged = { text ->
                    onEvent(OccasionScreenEvent.OnNumberOfMiceChanged(text))
                }
            )

            MyTextField(
                value = state.weatherText,
                placeholder = "Weather",
                error = state.weatherError,
                label = "Weather",
                onValueChanged = { text ->
                    onEvent(OccasionScreenEvent.OnWeatherChanged(text))
                }
            )

            MyTextField(
                value = state.temperatureText,
                placeholder = "12",
                error = state.temperatureError,
                label = "Temperature",
                onValueChanged = { text ->
                    onEvent(OccasionScreenEvent.OnTemperatureChanged(text))
                }
            )

            MyTextField(
                value = state.legitimationText,
                placeholder = "Dano",
                error = state.legitimationError,
                label = "Legitimation*",
                onValueChanged = { text ->
                    onEvent(OccasionScreenEvent.OnLegitimationChanged(text))
                }
            )

            MyTextField(
                value = state.noteText,
                placeholder = "Note",
                error = state.noteError,
                label = "Note",
                onValueChanged = { text ->
                    onEvent(OccasionScreenEvent.OnNoteChanged(text))
                }
            )
        }
    }
}