package com.example.datatrap.settings.envtype.presentation

import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datatrap.R
import com.example.datatrap.settings.envtype.data.EnvType
import com.example.datatrap.settings.envtype.data.EnvTypeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class EnvTypeViewModel @Inject constructor (
    private val envTypeRepository: EnvTypeRepository
) : ViewModel() {

    val envTypeList: LiveData<List<EnvType>> = envTypeRepository.envTypeList

    fun insertEnvType(envType: EnvType) {
        viewModelScope.launch(Dispatchers.IO) {
            envTypeRepository.insertEnvType(envType)
        }
    }

    fun updateEnvType(envType: EnvType) {
        viewModelScope.launch(Dispatchers.IO) {
            envTypeRepository.updateEnvType(envType)
        }
    }

    fun deleteEnvType(envType: EnvType) {
        viewModelScope.launch(Dispatchers.IO) {
            envTypeRepository.deleteEnvType(envType)
        }
    }

    private fun insertEnvType(name: String){
        if (name.isNotEmpty()){

            val envType: EnvType = EnvType(0, name, Calendar.getInstance().time, null)

            envTypeViewModel.insertEnvType(envType)

            Toast.makeText(requireContext(),"New environment type added.", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(requireContext(), getString(R.string.emptyFields), Toast.LENGTH_LONG).show()
        }
    }

    private fun updateEnvType(envType: EnvType, name: String){
        if (name.isNotEmpty()){

            val envTypeNew: EnvType = envType
            envTypeNew.envTypeName = name
            envTypeNew.envTypeDateTimeUpdated = Calendar.getInstance().time

            envTypeViewModel.updateEnvType(envTypeNew)

            Toast.makeText(requireContext(),"Environment type updated.", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(requireContext(), getString(R.string.emptyFields), Toast.LENGTH_LONG).show()
        }
    }

    private fun deleteEnvType(envType: EnvType, what: String, title: String) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes"){_, _ ->

            envTypeViewModel.deleteEnvType(envType)
            Toast.makeText(requireContext(),"$what deleted.", Toast.LENGTH_LONG).show()
        }
            .setNegativeButton("No"){_, _ -> }
            .setTitle("Delete $title")
            .setMessage("Are you sure you want to delete this $what ?")
            .create().show()
    }

    init {
        binding.addEnvtypeFloatButton.setOnClickListener {
            showAddDialog("New Environment Type", "Add new environment type?")
        }

        adapter.setOnItemClickListener(object: EnvTypeRecyclerAdapter.MyClickListener {
            override fun useClickListener(position: Int) {
                // update envType
                val currEnvType: EnvType = envTypeList[position]
                showUpdateDialog("Update Environment Type", "Update environment type?", currEnvType)
            }

            override fun useLongClickListener(position: Int) {
                // delete envType
                val envType: EnvType = envTypeList[position]

                deleteEnvType(envType, "Environment type", "Environment Type")
            }
        })
    }

    fun onEvent(event: EnvTypeListScreenEvent) {
        when(event) {
            is EnvTypeListScreenEvent.OnDeleteClick -> TODO()
            is EnvTypeListScreenEvent.OnInsertClick -> TODO()
            is EnvTypeListScreenEvent.OnItemClick -> TODO()
        }
    }

}