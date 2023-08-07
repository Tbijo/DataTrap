package com.example.datatrap.settings.methodtype.presentation

import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datatrap.R
import com.example.datatrap.settings.methodtype.data.MethodType
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

    val methodTypeList: LiveData<List<MethodType>> = methodTypeRepository.methodTypeList
    private lateinit var methodTypeList: List<MethodType>


    fun insertMethodType(methodType: MethodType) {
        viewModelScope.launch(Dispatchers.IO) {
            methodTypeRepository.insertMethodType(methodType)
        }
    }

    fun updateMethodType(methodType: MethodType) {
        viewModelScope.launch(Dispatchers.IO) {
            methodTypeRepository.updateMethodType(methodType)
        }
    }

    fun deleteMethodType(methodType: MethodType) {
        viewModelScope.launch(Dispatchers.IO) {
            methodTypeRepository.deleteMethodType(methodType)
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
                val currMetType: MethodType = methodTypeList[position]
                showUpdateDialog("Update Method Type", "Update method type?", currMetType)
            }

            override fun useLongClickListener(position: Int) {
                // delete method type
                val methodType: MethodType = methodTypeList[position]
                deleteEnvType(methodType, "Method type", "Method Type")
            }
        })
    }

    private fun insertMethodType(name: String){
        if (name.isNotEmpty()){

            val methodType: MethodType = MethodType(0, name, Calendar.getInstance().time, null)

            methodTypeViewModel.insertMethodType(methodType)

            Toast.makeText(requireContext(),"New method type added.", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(requireContext(), getString(R.string.emptyFields), Toast.LENGTH_LONG).show()
        }
    }
    private fun updateMethodType(methodType: MethodType, name: String){
        if (name.isNotEmpty()){

            val methodTypeNew: MethodType = methodType
            methodTypeNew.methodTypeName = name
            methodTypeNew.methTypeDateTimeUpdated = Calendar.getInstance().time

            methodTypeViewModel.updateMethodType(methodTypeNew)

            Toast.makeText(requireContext(),"Method type updated.", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(requireContext(), getString(R.string.emptyFields), Toast.LENGTH_LONG).show()
        }
    }

    private fun deleteEnvType(methodType: MethodType, what: String, title: String) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes"){_, _ ->

            methodTypeViewModel.deleteMethodType(methodType)
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