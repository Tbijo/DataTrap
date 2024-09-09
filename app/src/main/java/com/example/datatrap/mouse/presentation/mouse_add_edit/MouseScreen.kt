package com.example.datatrap.mouse.presentation.mouse_add_edit

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.example.datatrap.R
import com.example.datatrap.core.presentation.LoadingScreen
import com.example.datatrap.core.presentation.components.KeyType
import com.example.datatrap.core.presentation.components.MyBottomSheetScaffold
import com.example.datatrap.core.presentation.components.MyTextField
import com.example.datatrap.core.presentation.components.SuspendDialog
import com.example.datatrap.core.presentation.components.ToggleButton
import com.example.datatrap.core.util.EnumCaptureID
import com.example.datatrap.core.util.EnumMouseAge
import com.example.datatrap.core.util.EnumSex
import com.example.datatrap.mouse.presentation.mouse_add_edit.components.MouseSketch

@Composable
fun MouseScreen(
    onEvent: (MouseScreenEvent) -> Unit,
    state: MouseUiState,
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
    onEvent: (MouseScreenEvent) -> Unit,
    state: MouseUiState,
) {
    BackHandler {
        onEvent(MouseScreenEvent.OnLeave)
    }

    MyBottomSheetScaffold(
        title = "Mouse",
        errorState = state.error,
        isSheetExpanded = state.isSheetExpanded,
        actions = {
            IconButton(onClick = {
                onEvent(MouseScreenEvent.OnInsertClick)
            }) {
                Icon(imageVector = Icons.Default.Save, contentDescription = "save icon")
            }
            IconButton(onClick = {
                onEvent(MouseScreenEvent.OnCameraClick(state.mouseEntity?.mouseId))
            }) {
                Icon(imageVector = Icons.Default.Camera, contentDescription = "camera icon")
            }
            IconButton(onClick = {
                onEvent(MouseScreenEvent.OnMouseClick)
            }) {
                Icon(painter = painterResource(id = R.drawable.ic_rat), contentDescription = "rat icon")
            }
        },
        sheetContent = {
            state.specieEntity?.upperFingers?.let { fingers ->
                MouseSketch(
                    uniCode = state.code.toInt(),
                    fingers = fingers,
                )
            }
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .verticalScroll(rememberScrollState())
        ) {
            Row {
                MyTextField(value = state.code, placeholder = "301", error = state.codeError, label = "Mouse Code",
                    keyType = KeyType.NUMBER,
                    onValueChanged = { text ->
                        onEvent(MouseScreenEvent.OnCodeTextChanged(text))
                    }
                )
                Button(
                    onClick = {
                        onEvent(MouseScreenEvent.OnGenerateButtonClick)
                    },
                    // Disable when Update scenario
                    enabled = state.mouseEntity == null,
                ) {
                    Text(text = "Generate Code")
                }
            }

            Row {
                DropdownMenu(
                    modifier = Modifier.clickable { onEvent(MouseScreenEvent.OnSpecieDropDownClick) },
                    expanded = state.isSpecieExpanded,
                    onDismissRequest = { onEvent(MouseScreenEvent.OnSpecieDropDownDismiss) },
                ) {
                    state.mouseEntity?.let { mouse ->
                        val specieCode = state.specieList.find { specie -> specie.specieId == mouse.speciesID }?.speciesCode
                        DropdownMenuItem(
                            onClick = {},
                            enabled = false,
                            text = { Text(text = "$specieCode") },
                        )
                    }
                    DropdownMenuItem(
                        onClick = {},
                        enabled = false,
                        text = { Text(text = "Specie*") },
                    )
                    state.specieList.forEach { specie ->
                        DropdownMenuItem(
                            onClick = { onEvent(MouseScreenEvent.OnSelectSpecie(specie)) },
                            text = { Text(text = specie.speciesCode) }
                        )
                    }
                }

                DropdownMenu(
                    modifier = Modifier.clickable { onEvent(MouseScreenEvent.OnTrapIDDropDownClick) },
                    expanded = state.isTrapIDExpanded,
                    onDismissRequest = { onEvent(MouseScreenEvent.OnTrapIDDropDownDismiss) },
                ) {
                    state.mouseEntity?.let { mouse ->
                        DropdownMenuItem(
                            onClick = {},
                            enabled = false,
                            text = { Text(text = "${mouse.trapID}") },
                        )
                    }
                    DropdownMenuItem(
                        onClick = {},
                        enabled = false,
                        text = { Text(text = "TrapID") },
                    )
                    state.trapIDList.forEach { trapId ->
                        DropdownMenuItem(
                            onClick = { onEvent(MouseScreenEvent.OnSelectTrapID(trapId)) },
                            text = { Text(text = "$trapId") },
                        )
                    }
                }

                DropdownMenu(
                    modifier = Modifier.clickable { onEvent(MouseScreenEvent.OnProtocolDropDownClick) },
                    expanded = state.isProtocolExpanded,
                    onDismissRequest = { onEvent(MouseScreenEvent.OnProtocolDropDownDismiss) },
                ) {
                    state.mouseEntity?.let { mouse ->
                        val protocol = state.protocolList.find { protocol -> protocol.protocolId == mouse.protocolID }?.protocolName
                        DropdownMenuItem(
                            onClick = {},
                            enabled = false,
                            text = { Text(text = "$protocol") },
                        )
                    }
                    DropdownMenuItem(
                        onClick = {},
                        enabled = false,
                        text = { Text(text = "Protocol") },
                    )
                    state.protocolList.forEach { protocol ->
                        DropdownMenuItem(
                            onClick = { onEvent(MouseScreenEvent.OnSelectProtocol(protocol)) },
                            text = { Text(text = protocol.protocolName) },
                        )
                    }
                }
            }

            Row {
                // Sex
                ToggleButton(text = "Male", isSelected = state.sex == EnumSex.MALE) {
                    onEvent(MouseScreenEvent.OnSexClick(EnumSex.MALE))
                }
                ToggleButton(text = "Female", isSelected = state.sex == EnumSex.FEMALE) {
                    onEvent(MouseScreenEvent.OnSexClick(EnumSex.FEMALE))
                }
                ToggleButton(text = "N/A", isSelected = state.sex == null) {
                    onEvent(MouseScreenEvent.OnSexClick(null))
                }
            }

            Row {
                // Age
                ToggleButton(text = "Juvenile", isSelected = state.age == EnumMouseAge.JUVENILE) {
                    onEvent(MouseScreenEvent.OnAgeClick(EnumMouseAge.JUVENILE))
                }
                ToggleButton(text = "Subadult", isSelected = state.age == EnumMouseAge.SUBADULT) {
                    onEvent(MouseScreenEvent.OnAgeClick(EnumMouseAge.SUBADULT))
                }
                ToggleButton(text = "Adult", isSelected = state.age == EnumMouseAge.ADULT) {
                    onEvent(MouseScreenEvent.OnAgeClick(EnumMouseAge.ADULT))
                }
                ToggleButton(text = "N/A", isSelected = state.age == null) {
                    onEvent(MouseScreenEvent.OnAgeClick(null))
                }
            }

            Row {
                // CaptureID
                ToggleButton(text = "Died", isSelected = state.captureID == EnumCaptureID.DIED) {
                    onEvent(
                        MouseScreenEvent.OnCaptureIdClick(EnumCaptureID.DIED)
                    )
                }
                ToggleButton(text = "Captured", isSelected = state.captureID == EnumCaptureID.CAPTURED) {
                    onEvent(
                        MouseScreenEvent.OnCaptureIdClick(EnumCaptureID.CAPTURED)
                    )
                }
                ToggleButton(text = "Released", isSelected = state.captureID == EnumCaptureID.RELEASED) {
                    onEvent(
                        MouseScreenEvent.OnCaptureIdClick(EnumCaptureID.RELEASED)
                    )
                }
                ToggleButton(text = "Escaped", isSelected = state.captureID == EnumCaptureID.ESCAPED) {
                    onEvent(
                        MouseScreenEvent.OnCaptureIdClick(EnumCaptureID.ESCAPED)
                    )
                }
                ToggleButton(text = "N/A", isSelected = state.captureID == null) {
                    onEvent(
                        MouseScreenEvent.OnCaptureIdClick(captureID = null)
                    )
                }
            }

            Row {
                // Sexual Activity / checkboxes
                ToggleButton(text = "Sex. Active", isSelected = state.sexActive) {
                    onEvent(MouseScreenEvent.OnSexActiveClick)
                }
                ToggleButton(text = "Lactating", isSelected = state.lactating) {
                    onEvent(MouseScreenEvent.OnLactatingClick)
                }
                ToggleButton(text = "Gravidity", isSelected = state.gravidity) {
                    onEvent(MouseScreenEvent.OnGravidityClick)
                }
            }

            Row {
                MyTextField(value = state.body, placeholder = "11.3", error = state.bodyError, label = "Body",
                    keyType = KeyType.DECIMAL,
                    onValueChanged = { text ->
                        onEvent(MouseScreenEvent.OnBodyTextChanged(text))
                    }
                )
                MyTextField(value = state.tail, placeholder = "11.3", error = state.tailError, label = "Tail",
                    keyType = KeyType.DECIMAL,
                    onValueChanged = { text ->
                        onEvent(MouseScreenEvent.OnTailTextChanged(text))
                    }
                )
                MyTextField(value = state.feet, placeholder = "11.3", error = state.feetError, label = "Feet",
                    keyType = KeyType.DECIMAL,
                    onValueChanged = { text ->
                        onEvent(MouseScreenEvent.OnFeetTextChanged(text))
                    }
                )
                MyTextField(value = state.ear, placeholder = "11.3", error = state.earError, label = "Ear",
                    keyType = KeyType.DECIMAL,
                    onValueChanged = { text ->
                        onEvent(MouseScreenEvent.OnEarTextChanged(text))
                    }
                )
            }

            Row {
                MyTextField(value = state.weight, placeholder = "11.3", error = state.weightError, label = "Weight",
                    keyType = KeyType.DECIMAL,
                    onValueChanged = { text ->
                        onEvent(MouseScreenEvent.OnWeightTextChanged(text))
                    }
                )
                MyTextField(value = state.testesLength, placeholder = "11.3", error = state.testesLengthError, label = "Testes Length",
                    keyType = KeyType.DECIMAL,
                    onValueChanged = { text ->
                        onEvent(MouseScreenEvent.OnTestesLengthTextChanged(text))
                    }
                )
                MyTextField(value = state.testesWidth, placeholder = "11.3", error = state.testesWidthError, label = "Testes Width",
                    keyType = KeyType.DECIMAL,
                    onValueChanged = { text ->
                        onEvent(MouseScreenEvent.OnTestesWidthTextChanged(text))
                    }
                )
            }

            Row {
                MyTextField(value = state.rightEmbryo, placeholder = "11", error = state.rightEmbryoError, label = "Right Embryo",
                    keyType = KeyType.NUMBER,
                    onValueChanged = { text ->
                        onEvent(MouseScreenEvent.OnRightEmbryoTextChanged(text))
                    }
                )
                MyTextField(value = state.leftEmbryo, placeholder = "11", error = state.leftEmbryoError, label = "Left Embryo",
                    keyType = KeyType.NUMBER,
                    onValueChanged = { text ->
                        onEvent(MouseScreenEvent.OnLeftEmbryoTextChanged(text))
                    }
                )
                MyTextField(value = state.embryoDiameter, placeholder = "11.3", error = state.embryoDiameterError,
                    label = "Embryo Diameter",
                    keyType = KeyType.DECIMAL,
                    onValueChanged = { text ->
                        onEvent(MouseScreenEvent.OnEmbryoDiameterTextChanged(text))
                    }
                )
            }

            Row {
                MyTextField(value = state.mcRight, placeholder = "11", error = state.mcRightError, label = "MC Right",
                    keyType = KeyType.NUMBER,
                    onValueChanged = { text ->
                        onEvent(MouseScreenEvent.OnMcRightTextChanged(text))
                    }
                )
                MyTextField(value = state.mcLeft, placeholder = "11", error = state.mcLeftError, label = "MC Left",
                    keyType = KeyType.NUMBER,
                    onValueChanged = { text ->
                        onEvent(MouseScreenEvent.OnMcLeftTextChanged(text))
                    }
                )
                ToggleButton(text = "MC", isSelected = state.mc) {
                    onEvent(MouseScreenEvent.OnMcClick)
                }
            }

            MyTextField(value = state.note, placeholder = "Note... note", error = state.noteError, label = "Note",
                onValueChanged = { text ->
                    onEvent(MouseScreenEvent.OnNoteTextChanged(text))
                }
            )
        }

        if (state.isDialogShowing) {
            SuspendDialog(
                title = state.dialogTitle,
                message = state.dialogMessage,
                onOkClick = { onEvent(MouseScreenEvent.OnDialogOkClick) },
                onCancelClick = { onEvent(MouseScreenEvent.OnDialogCancelClick) },
                onDismiss = { onEvent(MouseScreenEvent.OnDialogDismiss) },
            )
        }
    }
}