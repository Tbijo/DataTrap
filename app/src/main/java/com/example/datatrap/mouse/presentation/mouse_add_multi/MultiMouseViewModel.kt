package com.example.datatrap.mouse.presentation.mouse_add_multi

import android.provider.Settings
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.datatrap.R
import com.example.datatrap.core.util.EnumSpecie
import com.example.datatrap.mouse.data.Mouse
import com.example.datatrap.mouse.domain.model.MultiMouse
import com.example.datatrap.mouse.presentation.mouse_list.MouseListViewModel
import com.example.datatrap.specie.data.SpecList
import com.example.datatrap.specie.presentation.specie_list.SpecieListViewModel
import java.util.Calendar

class MultiMouseViewModel: ViewModel() {

    private val mouseListViewModel: MouseListViewModel by viewModels()
    private val specieListViewModel: SpecieListViewModel by viewModels()

    private lateinit var listSpecie: List<SpecList>
    private lateinit var trapIdList: List<Int>

    private var specieList = EnumSpecie.values().map {
        it.name
    }

    private var size: Int = 1

    private var mapData: MutableMap<Int, MultiMouse> = mutableMapOf()

    private val dropDownArrTrapID = ArrayAdapter(context, R.layout.dropdown_names, trapIdList)
    private val dropDownArrSpecie = ArrayAdapter(context, R.layout.dropdown_names, specieList)

    init {
        val list = (1..args.occlist.numTraps).toList()
        adapter = MouseMultiRecyclerAdapter(list, requireContext())

        binding.btnAddRow.setOnClickListener {
            adapter.addRow()
        }

        binding.btnRmvRow.setOnClickListener {
            adapter.removeRow()
        }

        val listSpec = EnumSpecie.values().map { it.name }

        specieListViewModel.getNonSpecie(listSpec).observe(viewLifecycleOwner) {
            listSpecie = it
        }

        R.id.menu_save -> insertMouse()
    }

    private fun insertMouse() {
        val map = adapter.getData()

        if (!checkInput(map)) {
            return
        }

        val deviceID: String = Settings.Secure.getString(
            requireContext().contentResolver,
            Settings.Secure.ANDROID_ID
        )

        val multiMouse: MutableList<Mouse> = mutableListOf()

        map.forEach {
            val mouse = Mouse(
                0,
                0,
                null,
                deviceID,
                null,
                speciesID = getSpecieId(it.value.specieID!!)!!,
                null,
                occasionID = args.occlist.occasionId,
                localityID = args.occlist.localityID,
                trapID = it.value.trapID,
                Calendar.getInstance().time,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                Calendar.getInstance().time.time
            )

            multiMouse.add(mouse)
        }

        mouseListViewModel.insertMultiMouse(multiMouse)

        Toast.makeText(requireContext(), "Multiple mice added.", Toast.LENGTH_SHORT).show()

        findNavController().navigateUp()
    }

    private fun getSpecieId(specieCode: String): Long? =
        listSpecie.find {
            it.speciesCode == specieCode
        }?.specieId

    private fun checkInput(map: MutableMap<Int, MultiMouse>): Boolean {
        map.forEach {
            if (it.value.specieID == null) {
                Toast.makeText(
                    requireContext(),
                    "SpecieID at position ${it.key} not filled.",
                    Toast.LENGTH_LONG
                ).show()
                return false
            }
            if (it.value.trapID == null) {
                Toast.makeText(
                    requireContext(),
                    "TrapID at position ${it.key} not filled.",
                    Toast.LENGTH_LONG
                ).show()
                return false
            }
        }
        return true
    }

    // Adapter

    fun onBindViewHolder(holder: MouseMultiRecyclerAdapter.MyViewHolder, position: Int) {

        holder.binding.autoCompTvTrapId.setAdapter(dropDownArrTrapID)
        holder.binding.autoCompTvTrapId.setText("")

        holder.binding.autoCompTvSpecie.setAdapter(dropDownArrSpecie)
        holder.binding.autoCompTvSpecie.setText("")

        holder.binding.autoCompTvTrapId.setOnItemClickListener{ parent, _, itemPosition, _ ->
            val name: String = parent.getItemAtPosition(itemPosition).toString()
            if (mapData[position] == null) {
                val multiMouse = MultiMouse(Integer.parseInt(name), null)
                mapData[position] = multiMouse
            } else {
                mapData[position]?.trapID = Integer.parseInt(name)
            }
        }

        holder.binding.autoCompTvSpecie.setOnItemClickListener{ parent, _, itemPosition, _ ->
            val name: String = parent.getItemAtPosition(itemPosition) as String
            if (mapData[position] == null) {
                val multiMouse = MultiMouse(null, name)
                mapData[position] = multiMouse
            } else {
                mapData[position]?.specieID = name
            }
        }
    }
    fun addRow(){
        this.size += 1
        notifyItemInserted(size)
    }

    fun removeRow(){
        if (this.size - 1 >= 0) {
            this.size -= 1
            notifyItemRemoved(size)
            mapData.remove(size)
        }
    }

    fun getData() = mapData

}