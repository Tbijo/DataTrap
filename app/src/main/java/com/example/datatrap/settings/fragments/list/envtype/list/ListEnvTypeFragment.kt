package com.example.datatrap.settings.fragments.list.envtype.list

import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.datatrap.R
import com.example.datatrap.databinding.FragmentListEnvTypeBinding
import com.example.datatrap.models.EnvType
import com.example.datatrap.viewmodels.EnvTypeViewModel

class ListEnvTypeFragment : Fragment() {

    private var _binding: FragmentListEnvTypeBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: EnvTypeRecyclerAdapter
    private lateinit var envTypeViewModel: EnvTypeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        _binding = FragmentListEnvTypeBinding.inflate(inflater, container, false)
        envTypeViewModel = ViewModelProvider(this).get(EnvTypeViewModel::class.java)

        adapter = EnvTypeRecyclerAdapter()
        binding.envTypeRecyclerview.adapter = adapter
        binding.envTypeRecyclerview.layoutManager = LinearLayoutManager(requireContext())

        envTypeViewModel.envTypeList.observe(viewLifecycleOwner, Observer { envTypes ->
            adapter.setData(envTypes)
        })

        binding.addEnvtypeFloatButton.setOnClickListener {
            showAddDialog("New Environment Type", "Name", "Add new environment type?")
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun showAddDialog(title: String, hint: String, message: String){
        val input = EditText(requireContext())
        input.hint = hint
        input.inputType = InputType.TYPE_CLASS_TEXT

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(title)
            .setMessage(message)
            .setView(input)
            .setPositiveButton("OK") { _, _ ->
                val name = input.text.toString()
                insertEnvType(name)
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }
            .create().show()
    }

    private fun insertEnvType(name: String){
        if (name.isNotEmpty()){

            val envType: EnvType = EnvType(name)

            envTypeViewModel.insertEnvType(envType)

            Toast.makeText(requireContext(),"New environment type added.", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(requireContext(), getString(R.string.emptyFields), Toast.LENGTH_LONG).show()
        }
    }
}