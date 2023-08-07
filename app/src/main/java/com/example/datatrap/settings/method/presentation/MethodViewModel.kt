package com.example.datatrap.settings.method.presentation

import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datatrap.R
import com.example.datatrap.settings.method.data.Method
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

    private lateinit var methodList: List<Method>

    val methodList: LiveData<List<Method>> = methodRepository.methodList

    fun insertMethod(method: Method){
        viewModelScope.launch(Dispatchers.IO) {
            methodRepository.insertMethod(method)
        }
    }

    fun updateMethod(method: Method){
        viewModelScope.launch(Dispatchers.IO) {
            methodRepository.updateMethod(method)
        }
    }

    fun deleteMethod(method: Method){
        viewModelScope.launch(Dispatchers.IO) {
            methodRepository.deleteMethod(method)
        }
    }

    init {
        binding.addMethodFloatButton.setOnClickListener {
            showAddDialog("Add Method", "Add new Method?")
        }

        adapter.setOnItemClickListener(object : MethodRecyclerAdapter.MyClickListener {
            override fun useClickListener(position: Int) {
                val currMethod = methodList[position]
                showUpdateDialog("Update Method", "Update method?", currMethod)
            }

            override fun useLongClickListener(position: Int) {
                val method = methodList[position]
                deleteMethod(method, "method", "Method")
            }

        })
    }

    private fun insertMethod(name: String){
        if (name.isNotEmpty()){

            val method: Method = Method(0, name, Calendar.getInstance().time, null)

            methodViewModel.insertMethod(method)

            Toast.makeText(requireContext(),"New method added.", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(requireContext(), getString(R.string.emptyFields), Toast.LENGTH_LONG).show()
        }
    }

    private fun updateMethod(method: Method, name: String){
        if (name.isNotEmpty()){

            val methodNew: Method = method
            methodNew.methodName = name
            methodNew.methodDateTimeUpdated = Calendar.getInstance().time

            methodViewModel.updateMethod(methodNew)

            Toast.makeText(requireContext(),"Method updated.", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(requireContext(), getString(R.string.emptyFields), Toast.LENGTH_LONG).show()
        }
    }

    private fun deleteMethod(method: Method, what: String, title: String) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes"){_, _ ->

            methodViewModel.deleteMethod(method)
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