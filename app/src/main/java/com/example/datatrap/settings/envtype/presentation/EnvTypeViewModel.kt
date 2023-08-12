package com.example.datatrap.settings.envtype.presentation

import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datatrap.R
import com.example.datatrap.settings.envtype.data.EnvTypeEntity
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

    val envTypeEntityList: LiveData<List<EnvTypeEntity>> = envTypeRepository.envTypeEntityList

    fun insertEnvType(envTypeEntity: EnvTypeEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            envTypeRepository.insertEnvType(envTypeEntity)
        }
    }

    fun updateEnvType(envTypeEntity: EnvTypeEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            envTypeRepository.updateEnvType(envTypeEntity)
        }
    }

    fun deleteEnvType(envTypeEntity: EnvTypeEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            envTypeRepository.deleteEnvType(envTypeEntity)
        }
    }

    private fun insertEnvType(name: String){
        if (name.isNotEmpty()){

            val envTypeEntity: EnvTypeEntity = EnvTypeEntity(0, name, Calendar.getInstance().time, null)

            envTypeViewModel.insertEnvType(envTypeEntity)

            Toast.makeText(requireContext(),"New environment type added.", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(requireContext(), getString(R.string.emptyFields), Toast.LENGTH_LONG).show()
        }
    }

    private fun updateEnvType(envTypeEntity: EnvTypeEntity, name: String){
        if (name.isNotEmpty()){

            val envTypeEntityNew: EnvTypeEntity = envTypeEntity
            envTypeEntityNew.envTypeName = name
            envTypeEntityNew.envTypeDateTimeUpdated = Calendar.getInstance().time

            envTypeViewModel.updateEnvType(envTypeEntityNew)

            Toast.makeText(requireContext(),"Environment type updated.", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(requireContext(), getString(R.string.emptyFields), Toast.LENGTH_LONG).show()
        }
    }

    private fun deleteEnvType(envTypeEntity: EnvTypeEntity, what: String, title: String) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes"){_, _ ->

            envTypeViewModel.deleteEnvType(envTypeEntity)
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
                val currEnvTypeEntity: EnvTypeEntity = envTypeEntityList[position]
                showUpdateDialog("Update Environment Type", "Update environment type?", currEnvTypeEntity)
            }

            override fun useLongClickListener(position: Int) {
                // delete envType
                val envTypeEntity: EnvTypeEntity = envTypeEntityList[position]

                deleteEnvType(envTypeEntity, "Environment type", "Environment Type")
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