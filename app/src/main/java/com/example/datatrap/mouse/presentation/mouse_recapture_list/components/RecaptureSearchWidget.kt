package com.example.datatrap.mouse.presentation.mouse_recapture_list.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.datatrap.core.presentation.components.DateTimeWidget
import com.example.datatrap.core.presentation.components.KeyType
import com.example.datatrap.core.presentation.components.MyTextField
import com.example.datatrap.core.presentation.components.ToggleButton
import com.example.datatrap.core.util.EnumMouseAge
import com.example.datatrap.core.util.EnumSex
import com.example.datatrap.specie.data.SpecieEntity
import java.time.LocalDate

@Composable
fun RecaptureSearchHeader(
    specieList: List<SpecieEntity>,
    codeText: String,
    onCodeTextChanged: (String) -> Unit,

    onSpecieDropDownClick: () -> Unit,
    isSpecieDropDownExpanded: Boolean,
    onSpecieDismiss: () -> Unit,
    onSpecieSelect: (SpecieEntity) -> Unit,

    onSexClick: (EnumSex?) -> Unit,
    sexState: EnumSex?,

    onAgeClick: (EnumMouseAge?) -> Unit,
    ageState: EnumMouseAge?,

    onSexActiveClick: (Boolean) -> Unit,
    sexActiveState: Boolean,
    onLactatingClick: (Boolean) -> Unit,
    lactatingState: Boolean,
    onGravidityClick: (Boolean) -> Unit,
    gravidityState: Boolean,

    onSelectFromDate: (LocalDate) -> Unit,
    onSelectToDate: (LocalDate) -> Unit,

    onConfirmClick: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row {
            MyTextField(
                value = codeText, placeholder = "301", error = null, label = "Individual Code",
                keyType = KeyType.NUMBER,
                onValueChanged = onCodeTextChanged,
            )

            DropdownMenu(
                modifier = Modifier.clickable { onSpecieDropDownClick() },
                expanded = isSpecieDropDownExpanded,
                onDismissRequest = onSpecieDismiss,
            ) {
                DropdownMenuItem(onClick = {}, enabled = false) {
                    Text(text = "Specie")
                }
                specieList.forEach {
                    DropdownMenuItem(onClick = { onSpecieSelect(it) }) {
                        Text(text = it.speciesCode)
                    }
                }
            }
        }

        Row {
            // Sex
            ToggleButton(text = "Male", isSelected = sexState == EnumSex.MALE) {
                onSexClick(EnumSex.MALE)
            }
            ToggleButton(text = "Female", isSelected = sexState == EnumSex.FEMALE) {
                onSexClick(EnumSex.FEMALE)
            }
            ToggleButton(text = "N/A", isSelected = sexState == null) {
                onSexClick(null)
            }
        }

        Row {
            // Age
            ToggleButton(text = "Juvenile", isSelected = ageState == EnumMouseAge.JUVENILE) {
                onAgeClick(EnumMouseAge.JUVENILE)
            }
            ToggleButton(text = "Subadult", isSelected = ageState == EnumMouseAge.SUBADULT) {
                onAgeClick(EnumMouseAge.SUBADULT)
            }
            ToggleButton(text = "Adult", isSelected = ageState == EnumMouseAge.ADULT) {
                onAgeClick(EnumMouseAge.ADULT)
            }
            ToggleButton(text = "N/A", isSelected = ageState == null) {
                onAgeClick(null)
            }
        }

        Row {
            // Sexual Activity / checkbox
            ToggleButton(text = "Sex. Active", isSelected = sexActiveState) {
                onSexActiveClick(!sexActiveState)
            }
            ToggleButton(text = "Lactating", isSelected = lactatingState) {
                onLactatingClick(!lactatingState)
            }
            ToggleButton(text = "Gravidity", isSelected = gravidityState) {
                onGravidityClick(!gravidityState)
            }
        }

        Row {
            // Date From*
            DateTimeWidget(
                dateButtonText = "Pick date from",
                onSelectDate = {
                    it?.let {
                        onSelectFromDate(it)
                    }
                }
            )

            // Date To*
            DateTimeWidget(
                dateButtonText = "Pick date to",
                onSelectDate = {
                    it?.let {
                        onSelectToDate(it)
                    }
                }
            )
        }

        TextButton(onClick = {
            onConfirmClick()
        }) {
            Text(text = "OK")
        }
    }
}