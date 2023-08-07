package com.example.datatrap.settings.traptype.presentation

import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datatrap.R
import com.example.datatrap.settings.traptype.data.TrapType
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

    val trapTypeList: LiveData<List<TrapType>> = trapTypeRepository.trapTypeList
    private lateinit var trapTypeList: List<TrapType>


    fun insertTrapType(trapType: TrapType) {
        viewModelScope.launch(Dispatchers.IO) {
            trapTypeRepository.insertTrapType(trapType)
        }
    }

    fun updateTrapType(trapType: TrapType) {
        viewModelScope.launch(Dispatchers.IO) {
            trapTypeRepository.updateTrapType(trapType)
        }
    }

    fun deleteTrapType(trapType: TrapType) {
        viewModelScope.launch(Dispatchers.IO) {
            trapTypeRepository.deleteTrapType(trapType)
        }
    }

    init {
        trapTypeViewModel.trapTypeList.observe(viewLifecycleOwner) { trapTypes ->
            adapter.setData(trapTypes)
            trapTypeList = trapTypes
        }

        binding.addTraptypeFloatButton.setOnClickListener {
            showAddDialog("New Trap Type", "Add new trap type?")
        }

        adapter.setOnItemClickListener(object: TrapTypeRecyclerAdapter.MyClickListener {
            override fun useClickListener(position: Int) {
                // update trap Type
                val currTrapType: TrapType = trapTypeList[position]
                showUpdateDialog("Update Trap Type", "Update trap type?", currTrapType)
            }

            override fun useLongClickListener(position: Int) {
                // delete trap Type
                val trapType: TrapType = trapTypeList[position]

                deleteEnvType(trapType, "Trap type", "Trap Type")
            }
        })
    }

    private fun insertTrapType(name: String){
        if (name.isNotEmpty()){

            val trapType: TrapType = TrapType(0, name, Calendar.getInstance().time, null)

            trapTypeViewModel.insertTrapType(trapType)

            Toast.makeText(requireContext(),"New trap type added.", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(requireContext(), getString(R.string.emptyFields), Toast.LENGTH_LONG).show()
        }
    }
    private fun updateTrapType(trapType: TrapType, name: String){
        if (name.isNotEmpty()){

            val trapTypeNew: TrapType = trapType
            trapTypeNew.trapTypeName = name
            trapTypeNew.trapTypeDateTimeUpdated = Calendar.getInstance().time

            trapTypeViewModel.updateTrapType(trapTypeNew)

            Toast.makeText(requireContext(),"Trap type updated.", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(requireContext(), getString(R.string.emptyFields), Toast.LENGTH_LONG).show()
        }
    }

    private fun deleteEnvType(trapType: TrapType, what: String, title: String) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes"){_, _ ->

            trapTypeViewModel.deleteTrapType(trapType)
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