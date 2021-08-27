package com.example.datatrap.settings.fragments.list.method.list

import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.datatrap.R
import com.example.datatrap.databinding.FragmentListMethodBinding
import com.example.datatrap.models.Method
import com.example.datatrap.settings.fragments.list.envtype.list.AnyRecyclerAdapter
import com.example.datatrap.viewmodels.MethodViewModel

class ListMethodFragment : Fragment() {

    private var _binding: FragmentListMethodBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: AnyRecyclerAdapter
    private lateinit var methodViewModel: MethodViewModel
    private lateinit var methodList: List<Method>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        _binding = FragmentListMethodBinding.inflate(inflater, container, false)
        methodViewModel = ViewModelProvider(this).get(MethodViewModel::class.java)

        adapter = AnyRecyclerAdapter()
        binding.methodRecyclerview.adapter = adapter
        binding.methodRecyclerview.layoutManager = LinearLayoutManager(requireContext())

        methodViewModel.methodList.observe(viewLifecycleOwner, Observer {
            adapter.setData(it)
            methodList = it
        })

        binding.addMethodFloatButton.setOnClickListener {
            showAddDialog("Add Method", "Add new Method?")
        }

        adapter.setOnItemClickListener(object : AnyRecyclerAdapter.MyClickListener{
            override fun useClickListener(position: Int) {
                val currName = methodList[position]
                showUpdateDialog("Update Method", "Update method?", currName.methodName)
            }

            override fun useLongClickListener(position: Int) {
                val method = methodList[position]
                deleteMethod(method, "method", "Method")
            }

        })

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun showAddDialog(title: String, message: String){
        val input = EditText(requireContext())
        input.hint = "Name"
        input.inputType = InputType.TYPE_CLASS_TEXT

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(title)
            .setMessage(message)
            .setView(input)
            .setPositiveButton("OK") { _, _ ->
                val name = input.text.toString()
                insertMethod(name)
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }
            .create().show()
    }

    private fun insertMethod(name: String){
        if (name.isNotEmpty()){

            val method: Method = Method(name)

            methodViewModel.insertMethod(method)

            Toast.makeText(requireContext(),"New method added.", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(requireContext(), getString(R.string.emptyFields), Toast.LENGTH_LONG).show()
        }
    }

    private fun showUpdateDialog(title: String, message: String, currName: String){
        val input = EditText(requireContext())
        input.setText(currName)
        input.hint = "Name"
        input.inputType = InputType.TYPE_CLASS_TEXT

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(title)
            .setMessage(message)
            .setView(input)
            .setPositiveButton("OK") { _, _ ->
                val name = input.text.toString()
                updateMethod(name)
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }
            .create().show()
    }

    private fun updateMethod(name: String){
        if (name.isNotEmpty()){

            val method: Method = Method(name)

            methodViewModel.updateMethod(method)

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

}