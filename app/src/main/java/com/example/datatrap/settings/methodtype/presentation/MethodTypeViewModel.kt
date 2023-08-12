package com.example.datatrap.settings.methodtype.presentation

import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datatrap.R
import com.example.datatrap.settings.methodtype.data.MethodTypeEntity
import com.example.datatrap.settings.methodtype.data.MethodTypeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class MethodTypeViewModel @Inject constructor(
    private val methodTypeRepository: MethodTypeRepository
) : ViewModel() {

    val methodTypeEntityList: LiveData<List<MethodTypeEntity>> = methodTypeRepository.methodTypeEntityList
    private lateinit var methodTypeEntityList: List<MethodTypeEntity>


    fun insertMethodType(methodTypeEntity: MethodTypeEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            methodTypeRepository.insertMethodType(methodTypeEntity)
        }
    }

    fun updateMethodType(methodTypeEntity: MethodTypeEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            methodTypeRepository.updateMethodType(methodTypeEntity)
        }
    }

    fun deleteMethodType(methodTypeEntity: MethodTypeEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            methodTypeRepository.deleteMethodType(methodTypeEntity)
        }
    }

    init {
        binding.addMethodtypeFloatButton.setOnClickListener {
            // add method type
            showAddDialog("New Method Type", "Add new method type?")
        }

        adapter.setOnItemClickListener(object: MetTypeRecyclerAdapter.MyClickListener {
            override fun useClickListener(position: Int) {
                // update method type
                val currMetType: MethodTypeEntity = methodTypeEntityList[position]
                showUpdateDialog("Update Method Type", "Update method type?", currMetType)
            }

            override fun useLongClickListener(position: Int) {
                // delete method type
                val methodTypeEntity: MethodTypeEntity = methodTypeEntityList[position]
                deleteEnvType(methodTypeEntity, "Method type", "Method Type")
            }
        })
    }

    private fun insertMethodType(name: String){
        if (name.isNotEmpty()){

            val methodTypeEntity: MethodTypeEntity = MethodTypeEntity(0, name, Calendar.getInstance().time, null)

            methodTypeViewModel.insertMethodType(methodTypeEntity)

            Toast.makeText(requireContext(),"New method type added.", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(requireContext(), getString(R.string.emptyFields), Toast.LENGTH_LONG).show()
        }
    }
    private fun updateMethodType(methodTypeEntity: MethodTypeEntity, name: String){
        if (name.isNotEmpty()){

            val methodTypeEntityNew: MethodTypeEntity = methodTypeEntity
            methodTypeEntityNew.methodTypeName = name
            methodTypeEntityNew.methTypeDateTimeUpdated = Calendar.getInstance().time

            methodTypeViewModel.updateMethodType(methodTypeEntityNew)

            Toast.makeText(requireContext(),"Method type updated.", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(requireContext(), getString(R.string.emptyFields), Toast.LENGTH_LONG).show()
        }
    }

    private fun deleteEnvType(methodTypeEntity: MethodTypeEntity, what: String, title: String) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes"){_, _ ->

            methodTypeViewModel.deleteMethodType(methodTypeEntity)
            Toast.makeText(requireContext(),"$what deleted.", Toast.LENGTH_LONG).show()
        }
            .setNegativeButton("No"){_, _ -> }
            .setTitle("Delete $title")
            .setMessage("Are you sure you want to delete this $what ?")
            .create().show()
    }

    fun onEvent(event: MethodTypeListScreenEvent) {
        when(event) {
            is MethodTypeListScreenEvent.OnDeleteClick -> TODO()
            is MethodTypeListScreenEvent.OnInsertClick -> TODO()
            is MethodTypeListScreenEvent.OnItemClick -> TODO()
        }
    }
}