package com.example.datatrap.mouse.presentation.mouse_recapture_list.components

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.fragment.app.DialogFragment
import com.example.datatrap.R
import com.example.datatrap.core.presentation.components.MyTextField
import com.example.datatrap.core.presentation.components.ToggleButton
import com.example.datatrap.mouse.domain.model.SearchMouse
import com.example.datatrap.core.util.EnumMouseAge
import com.example.datatrap.core.util.EnumSex
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun RecaptureSearchWidget(specieMap: Map<String, Long>) {
    var sex: String? = null
    var age: String? = null
    var speciesID: Long? = null
    var dateFrom: Long? = null
    var dateTo: Long? = null
    var code: String? = null

    Column {
        Row {
            MyTextField(value = "301", placeholder = "301", error = null, label = "Individual Code",
                onValueChanged = {
                    // TODO
                }
            )

            DropdownMenu(expanded = true, onDismissRequest = { /*TODO*/ }) {
                DropdownMenuItem(onClick = { /*TODO*/ }) {
                    Text(text = "Specie")
                }
            }

            Row {
                // Sex
                ToggleButton(text = "Male", isSelected = false) {
                    // TODO
                }
                ToggleButton(text = "Female", isSelected = false) {
                    // TODO
                }
                ToggleButton(text = "N/A", isSelected = false) {
                    // TODO
                }
            }

            Row {
                // Age
                ToggleButton(text = "Juvenile", isSelected = false) {
                    // TODO
                }
                ToggleButton(text = "Subadult", isSelected = false) {
                    // TODO
                }
                ToggleButton(text = "Adult", isSelected = false) {
                    // TODO
                }
                ToggleButton(text = "N/A", isSelected = false) {
                    // TODO
                }
            }

            Row {
                // Sexual Activity / checkbox
                ToggleButton(text = "Sex. Active", isSelected = false) {
                    // TODO
                }
                ToggleButton(text = "Lactating", isSelected = false) {
                    // TODO
                }
                ToggleButton(text = "Gravidity", isSelected = false) {
                    // TODO
                }
            }

            Row {
                // Date From*
                DateTimePicker()

                // Date To*
                DateTimePicker()
            }
        }
    }
}

fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    _binding = FragmentSearchRecaptureBinding.inflate(layoutInflater)
    fillDropDown()
    setListeners()
    initInterface()
    return AlertDialog.Builder(requireActivity())
        .setView(binding.root)
        .setPositiveButton("OK") { _, _ ->
            code = binding.etSrchCode.text.toString()
            val gravidity = binding.cbGravit.isChecked
            val sexActive = binding.cbSexActive.isChecked
            val lactating = binding.cbLactating.isChecked

            // ak jeden datum bude null tak aj druhy musi byt
            if (dateFrom == null) dateTo = null
            if (dateTo == null) dateFrom = null

            val mouse = SearchMouse(
                if (!code.isNullOrBlank()) Integer.parseInt(code!!) else null,
                sex,
                age,
                speciesID,
                dateFrom,
                dateTo,
                gravidity,
                sexActive,
                lactating
            )
            listener.onDialogPositiveClick(mouse)

        }
        .setNegativeButton("Cancel") { _, _ ->
            listener.onDialogNegativeClick()
        }
        .create()
}

fun initInterface() {
    try {
        // kedze idem cez fragment tak treba fragment nie context ako pri aktivite
        listener = requireParentFragment() as DialogListener
    } catch (e: ClassCastException) {
        Log.e("SearchRecaptureFragment", "Must implement DialogListener.")
    }
}

fun fillDropDown() {
    val listCode = specieMap.keys.toList()
    val dropDownArrSpecie =
        ArrayAdapter(requireContext(), R.layout.dropdown_names, listCode)
    binding.acTvSpecie.setAdapter(dropDownArrSpecie)
}

fun setListeners() {
    binding.acTvSpecie.setOnItemClickListener { parent, _, position, _ ->
        val name: String = parent.getItemAtPosition(position) as String
        speciesID = specieMap[name]!!
    }

    binding.rgSex.setOnCheckedChangeListener { _, radioButtonId ->
        when (radioButtonId) {
            binding.rbMale.id -> sex = EnumSex.MALE.myName
            binding.rbFemale.id -> sex = EnumSex.FEMALE.myName
            binding.rbNullSex.id -> sex = null
        }
    }

    binding.rgAge.setOnCheckedChangeListener { _, radioButtonId ->
        when (radioButtonId) {
            binding.rbAdult.id -> age = EnumMouseAge.ADULT.myName
            binding.rbJuvenile.id -> age = EnumMouseAge.JUVENILE.myName
            binding.rbSubadult.id -> age = EnumMouseAge.SUBADULT.myName
            binding.rbNullAge.id -> age = null
        }
    }

    binding.cvDatetimeFrom.setOnDateChangeListener { _, year, month, day ->
        val dtStart = "$year-${month + 1}-${day}"
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.forLanguageTag("SK"))
        try {
            dateFrom = format.parse(dtStart)?.time ?: 0L
        } catch (e: ParseException) {
            Log.e("DateFormat", "DateParser")
        }
    }

    binding.cvDatetimeTo.setOnDateChangeListener { _, year, month, day ->
        val dtStart = "$year-${month + 1}-${day}"
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.forLanguageTag("SK"))
        try {
            dateTo = format.parse(dtStart)?.time ?: 0L
        } catch (e: ParseException) {
            Log.e("DateFormat", "DateParser")
        }
    }
}