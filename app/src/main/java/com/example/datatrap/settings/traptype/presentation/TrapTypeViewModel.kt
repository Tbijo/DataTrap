package com.example.datatrap.settings.traptype.presentation

import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datatrap.R
import com.example.datatrap.settings.traptype.data.TrapTypeEntity
import com.example.datatrap.settings.traptype.data.TrapTypeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class TrapTypeViewModel @Inject constructor(
    private val trapTypeRepository: TrapTypeRepository
): ViewModel() {

    val trapTypeEntityList: LiveData<List<TrapTypeEntity>> = trapTypeRepository.trapTypeEntityList
    private lateinit var trapTypeEntityList: List<TrapTypeEntity>


    fun insertTrapType(trapTypeEntity: TrapTypeEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            trapTypeRepository.insertTrapType(trapTypeEntity)
        }
    }

    fun updateTrapType(trapTypeEntity: TrapTypeEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            trapTypeRepository.updateTrapType(trapTypeEntity)
        }
    }

    fun deleteTrapType(trapTypeEntity: TrapTypeEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            trapTypeRepository.deleteTrapType(trapTypeEntity)
        }
    }

    init {
        trapTypeViewModel.trapTypeList.observe(viewLifecycleOwner) { trapTypes ->
            adapter.setData(trapTypes)
            trapTypeEntityList = trapTypes
        }

        binding.addTraptypeFloatButton.setOnClickListener {
            showAddDialog("New Trap Type", "Add new trap type?")
        }

        adapter.setOnItemClickListener(object: TrapTypeRecyclerAdapter.MyClickListener {
            override fun useClickListener(position: Int) {
                // update trap Type
                val currTrapTypeEntity: TrapTypeEntity = trapTypeEntityList[position]
                showUpdateDialog("Update Trap Type", "Update trap type?", currTrapTypeEntity)
            }

            override fun useLongClickListener(position: Int) {
                // delete trap Type
                val trapTypeEntity: TrapTypeEntity = trapTypeEntityList[position]

                deleteEnvType(trapTypeEntity, "Trap type", "Trap Type")
            }
        })
    }

    private fun insertTrapType(name: String){
        if (name.isNotEmpty()){

            val trapTypeEntity: TrapTypeEntity = TrapTypeEntity(0, name, Calendar.getInstance().time, null)

            trapTypeViewModel.insertTrapType(trapTypeEntity)

            Toast.makeText(requireContext(),"New trap type added.", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(requireContext(), getString(R.string.emptyFields), Toast.LENGTH_LONG).show()
        }
    }
    private fun updateTrapType(trapTypeEntity: TrapTypeEntity, name: String){
        if (name.isNotEmpty()){

            val trapTypeEntityNew: TrapTypeEntity = trapTypeEntity
            trapTypeEntityNew.trapTypeName = name
            trapTypeEntityNew.trapTypeDateTimeUpdated = Calendar.getInstance().time

            trapTypeViewModel.updateTrapType(trapTypeEntityNew)

            Toast.makeText(requireContext(),"Trap type updated.", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(requireContext(), getString(R.string.emptyFields), Toast.LENGTH_LONG).show()
        }
    }

    private fun deleteEnvType(trapTypeEntity: TrapTypeEntity, what: String, title: String) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes"){_, _ ->

            trapTypeViewModel.deleteTrapType(trapTypeEntity)
            Toast.makeText(requireContext(),"$what deleted.", Toast.LENGTH_LONG).show()
        }
            .setNegativeButton("No"){_, _ -> }
            .setTitle("Delete $title")
            .setMessage("Are you sure you want to delete this $what ?")
            .create().show()
    }

    fun onEvent(event: TrapTypeListScreenEvent) {
        when(event) {
            is TrapTypeListScreenEvent.OnDeleteClick -> TODO()
            is TrapTypeListScreenEvent.OnInsertClick -> TODO()
            is TrapTypeListScreenEvent.OnItemClick -> TODO()
        }
    }
}