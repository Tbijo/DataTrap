package com.example.datatrap.settings.method.presentation

import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datatrap.R
import com.example.datatrap.settings.method.data.MethodEntity
import com.example.datatrap.settings.method.data.MethodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class MethodViewModel @Inject constructor(
    private val methodRepository: MethodRepository
): ViewModel() {

    private lateinit var methodEntityList: List<MethodEntity>

    val methodEntityList: LiveData<List<MethodEntity>> = methodRepository.methodEntityList

    fun insertMethod(methodEntity: MethodEntity){
        viewModelScope.launch(Dispatchers.IO) {
            methodRepository.insertMethod(methodEntity)
        }
    }

    fun updateMethod(methodEntity: MethodEntity){
        viewModelScope.launch(Dispatchers.IO) {
            methodRepository.updateMethod(methodEntity)
        }
    }

    fun deleteMethod(methodEntity: MethodEntity){
        viewModelScope.launch(Dispatchers.IO) {
            methodRepository.deleteMethod(methodEntity)
        }
    }

    init {
        binding.addMethodFloatButton.setOnClickListener {
            showAddDialog("Add Method", "Add new Method?")
        }

        adapter.setOnItemClickListener(object : MethodRecyclerAdapter.MyClickListener {
            override fun useClickListener(position: Int) {
                val currMethod = methodEntityList[position]
                showUpdateDialog("Update Method", "Update method?", currMethod)
            }

            override fun useLongClickListener(position: Int) {
                val method = methodEntityList[position]
                deleteMethod(method, "method", "Method")
            }

        })
    }

    private fun insertMethod(name: String){
        if (name.isNotEmpty()){

            val methodEntity: MethodEntity = MethodEntity(0, name, Calendar.getInstance().time, null)

            methodViewModel.insertMethod(methodEntity)

            Toast.makeText(requireContext(),"New method added.", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(requireContext(), getString(R.string.emptyFields), Toast.LENGTH_LONG).show()
        }
    }

    private fun updateMethod(methodEntity: MethodEntity, name: String){
        if (name.isNotEmpty()){

            val methodEntityNew: MethodEntity = methodEntity
            methodEntityNew.methodName = name
            methodEntityNew.methodDateTimeUpdated = Calendar.getInstance().time

            methodViewModel.updateMethod(methodEntityNew)

            Toast.makeText(requireContext(),"Method updated.", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(requireContext(), getString(R.string.emptyFields), Toast.LENGTH_LONG).show()
        }
    }

    private fun deleteMethod(methodEntity: MethodEntity, what: String, title: String) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes"){_, _ ->

            methodViewModel.deleteMethod(methodEntity)
            Toast.makeText(requireContext(),"$what deleted.", Toast.LENGTH_LONG).show()
        }
            .setNegativeButton("No"){_, _ -> }
            .setTitle("Delete $title")
            .setMessage("Are you sure you want to delete this $what ?")
            .create().show()
    }

    fun onEvent(event: MethodListScreenEvent) {
        when(event) {
            is MethodListScreenEvent.OnDeleteClick -> TODO()
            is MethodListScreenEvent.OnInsertClick -> TODO()
            is MethodListScreenEvent.OnItemClick -> TODO()
        }
    }
}