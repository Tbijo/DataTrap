package com.example.datatrap.settings.vegettype.presentation

import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datatrap.R
import com.example.datatrap.settings.vegettype.data.VegetTypeEntity
import com.example.datatrap.settings.vegettype.data.VegetTypeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class VegetTypeViewModel @Inject constructor(
    private val vegetTypeRepository: VegetTypeRepository
): ViewModel() {

    val vegetTypeEntityList: LiveData<List<VegetTypeEntity>> = vegetTypeRepository.vegetTypeEntityList
    private lateinit var vegTypeList: List<VegetTypeEntity>


    fun insertVegetType(vegetTypeEntity: VegetTypeEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            vegetTypeRepository.insertVegetType(vegetTypeEntity)
        }
    }

    fun updateVegetType(vegetTypeEntity: VegetTypeEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            vegetTypeRepository.updateVegetType(vegetTypeEntity)
        }
    }

    fun deleteVegetType(vegetTypeEntity: VegetTypeEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            vegetTypeRepository.deleteVegetType(vegetTypeEntity)
        }
    }

    init {
        vegTypeViewModel.vegetTypeList.observe(viewLifecycleOwner) { vegTypes ->
            adapter.setData(vegTypes)
            vegTypeList = vegTypes
        }

        binding.addVegtypeFloatButton.setOnClickListener {
            showAddDialog("New Vegetation Type", "Add new vegetation type?")
        }

        adapter.setOnItemClickListener(object: VegTypeRecyclerAdapter.MyClickListener {
            override fun useClickListener(position: Int) {
                // update veg Type
                val currVegetTypeEntity: VegetTypeEntity = vegTypeList[position]
                showUpdateDialog("Update Vegetation Type", "Update vegetation type?", currVegetTypeEntity)
            }

            override fun useLongClickListener(position: Int) {
                // delete veg Type
                val vegType: VegetTypeEntity = vegTypeList[position]
                deleteEnvType(vegType, "Vegetation type", "Vegetation Type")
            }
        })
    }

    private fun insertVegType(name: String){
        if (name.isNotEmpty()){

            val vegType: VegetTypeEntity = VegetTypeEntity(0, name, Calendar.getInstance().time, null)

            vegTypeViewModel.insertVegetType(vegType)

            Toast.makeText(requireContext(),"New vegetation type added.", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(requireContext(), getString(R.string.emptyFields), Toast.LENGTH_LONG).show()
        }
    }
    private fun updateVegType(vegType: VegetTypeEntity, name: String){
        if (name.isNotEmpty()){

            val vegTypeNew: VegetTypeEntity = vegType
            vegTypeNew.vegetTypeName = name
            vegTypeNew.vegTypeDateTimeUpdated = Calendar.getInstance().time

            vegTypeViewModel.updateVegetType(vegTypeNew)

            Toast.makeText(requireContext(),"Vegetation type updated.", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(requireContext(), getString(R.string.emptyFields), Toast.LENGTH_LONG).show()
        }
    }

    private fun deleteEnvType(vegType: VegetTypeEntity, what: String, title: String) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes"){_, _ ->

            vegTypeViewModel.deleteVegetType(vegType)
            Toast.makeText(requireContext(),"$what deleted.", Toast.LENGTH_LONG).show()
        }
            .setNegativeButton("No"){_, _ -> }
            .setTitle("Delete $title")
            .setMessage("Are you sure you want to delete this $what ?")
            .create().show()
    }

    fun onEvent(event: VegetTypeListScreenEvent) {
        when(event) {
            is VegetTypeListScreenEvent.OnDeleteClick -> TODO()
            is VegetTypeListScreenEvent.OnInsertClick -> TODO()
            is VegetTypeListScreenEvent.OnItemClick -> TODO()
        }
    }
}