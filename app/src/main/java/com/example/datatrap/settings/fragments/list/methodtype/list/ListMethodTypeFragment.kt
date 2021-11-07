package com.example.datatrap.settings.fragments.list.methodtype.list

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
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.datatrap.R
import com.example.datatrap.databinding.FragmentListMethodTypeBinding
import com.example.datatrap.models.MethodType
import com.example.datatrap.settings.fragments.list.envtype.list.EnvTypeRecyclerAdapter
import com.example.datatrap.viewmodels.MethodTypeViewModel
import java.util.*

class ListMethodTypeFragment : Fragment() {

    private var _binding: FragmentListMethodTypeBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: MetTypeRecyclerAdapter
    private lateinit var methodTypeViewModel: MethodTypeViewModel
    private lateinit var methodTypeList: List<MethodType>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        _binding = FragmentListMethodTypeBinding.inflate(inflater, container, false)
        methodTypeViewModel = ViewModelProvider(this).get(MethodTypeViewModel::class.java)

        adapter = MetTypeRecyclerAdapter()
        binding.methodTypeRecyclerview.adapter = adapter
        binding.methodTypeRecyclerview.layoutManager = LinearLayoutManager(requireContext())

        val dividerItemDecoration = DividerItemDecoration(
            binding.methodTypeRecyclerview.context,
            LinearLayoutManager.VERTICAL
        )
        binding.methodTypeRecyclerview.addItemDecoration(dividerItemDecoration)

        methodTypeViewModel.methodTypeList.observe(viewLifecycleOwner, { methodTypes ->
            adapter.setData(methodTypes)
            methodTypeList = methodTypes
        })

        binding.addMethodtypeFloatButton.setOnClickListener {
            // add method type
            showAddDialog("New Method Type", "Add new method type?")
        }

        adapter.setOnItemClickListener(object: MetTypeRecyclerAdapter.MyClickListener{
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
                insertMethodType(name)
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }
            .create().show()
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

    private fun showUpdateDialog(title: String, message: String, methodType: MethodType){
        val input = EditText(requireContext())
        input.setText(methodType.methodTypeName)
        input.hint = "Name"
        input.inputType = InputType.TYPE_CLASS_TEXT

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(title)
            .setMessage(message)
            .setView(input)
            .setPositiveButton("OK") { _, _ ->
                val name = input.text.toString()
                updateMethodType(methodType, name)
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }
            .create().show()
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

}