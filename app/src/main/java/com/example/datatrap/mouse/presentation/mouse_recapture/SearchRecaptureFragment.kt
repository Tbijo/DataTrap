package com.example.datatrap.mouse.fragments.recapture

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import com.example.datatrap.R
import com.example.datatrap.databinding.FragmentSearchRecaptureBinding
import com.example.datatrap.mouse.data.SearchMouse
import com.example.datatrap.core.util.EnumMouseAge
import com.example.datatrap.core.util.EnumSex
import dagger.hilt.android.AndroidEntryPoint
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class SearchRecaptureFragment(private val specieMap: Map<String, Long>) : DialogFragment() {

    private var _binding: FragmentSearchRecaptureBinding? = null
    private val binding get() = _binding!!

    interface DialogListener {
        fun onDialogPositiveClick(searchMouse: SearchMouse)
        fun onDialogNegativeClick()
    }

    private lateinit var listener: DialogListener

    private var sex: String? = null
    private var age: String? = null
    private var speciesID: Long? = null
    private var dateFrom: Long? = null
    private var dateTo: Long? = null
    private var code: String? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
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

    private fun initInterface() {
        try {
            // kedze idem cez fragment tak treba fragment nie context ako pri aktivite
            listener = requireParentFragment() as DialogListener
        } catch (e: ClassCastException) {
            Log.e("SearchRecaptureFragment", "Must implement DialogListener.")
        }
    }

    override fun onResume() {
        super.onResume()
        fillDropDown()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun fillDropDown() {
        val listCode = specieMap.keys.toList()
        val dropDownArrSpecie =
            ArrayAdapter(requireContext(), R.layout.dropdown_names, listCode)
        binding.acTvSpecie.setAdapter(dropDownArrSpecie)
    }

    private fun setListeners() {
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

}