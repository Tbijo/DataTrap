package com.example.datatrap.core.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun DateTimeWidget(
//    onSelectTime: (LocalTime?) -> Unit,
    dateButtonText: String = "Pick a date",
    onSelectDate: (LocalDate?) -> Unit,
) {
    // dialog windows
    val dateDialogState = rememberMaterialDialogState()
//    val timeDialogState = rememberMaterialDialogState()

    var newDate: LocalDate? = null
//    var newTime: LocalTime? = null

    var pickedDate: LocalDate? by remember {
        mutableStateOf(LocalDate.now())
    }
//    var pickedTime: LocalTime? by remember {
//        mutableStateOf(LocalTime.now())
//    }

    val formattedDate by remember {
        // derived - creates a state from another state
        derivedStateOf {
            pickedDate?.let {
                DateTimeFormatter.ISO_LOCAL_DATE.format(it)
            } ?: "No date selected."
        }
    }
//    val formattedTime by remember {
//        derivedStateOf {
//            pickedTime?.let {
//                DateTimeFormatter.ISO_LOCAL_TIME.format(it)
//            } ?: "No time selected."
//        }
//    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround,
    ) {
        Column {
            Button(onClick = {
                // display dialog windows
                dateDialogState.show()
            }) {
                Text(text = dateButtonText)
            }
            Text(text = formattedDate)
        }

//        Column {
//            Button(onClick = {
//                timeDialogState.show()
//            }) {
//                Text(text = "Pick a time")
//            }
//            Text(text = formattedTime)
//        }
    }

    MaterialDialog(
        dialogState = dateDialogState,
        buttons = {
            // dismisses self automatically
            positiveButton(text = "Ok") {
                pickedDate = newDate
                onSelectDate(pickedDate)
            }
            negativeButton(text = "Cancel") {
                onSelectDate(null)
                pickedDate = null
            }
        },
    ) {
        // Type of Dialog
        datepicker(
            initialDate = LocalDate.now(),
            title = "Pick a date",
           
            // specify conditions on what date can be picked
            // now we can only select odd dates
            //it.dayOfMonth % 2 == 1
            //allowedDateValidator
        ) {
            // get the selected date
            newDate = it
        }
    }

//    MaterialDialog(
//        dialogState = timeDialogState,
//        buttons = {
//            positiveButton(text = "Ok") {
//                pickedTime = newTime
//                onSelectTime(pickedTime)
//            }
//            negativeButton(text = "Cancel") {
//                onSelectTime(null)
//                pickedTime = null
//            }
//        },
//    ) {
//        timepicker(
//            initialTime = LocalTime.now(),
//            is24HourClock = true,
//            title = "Pick a time",
//            // timeRange which time is possible to pick
//        ) {
//            // get the selected time
//            newTime = it
//        }
//    }
}