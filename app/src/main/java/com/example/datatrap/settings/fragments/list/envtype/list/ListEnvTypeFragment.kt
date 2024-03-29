package com.example.datatrap.settings.fragments.list.envtype.list

import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.datatrap.R
import com.example.datatrap.databinding.FragmentListEnvTypeBinding
import com.example.datatrap.models.EnvType
import com.example.datatrap.viewmodels.EnvTypeViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class ListEnvTypeFragment : Fragment() {

    private var _binding: FragmentListEnvTypeBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: EnvTypeRecyclerAdapter
    private val envTypeViewModel: EnvTypeViewModel by viewModels()
    private lateinit var envTypeList: List<EnvType>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        _binding = FragmentListEnvTypeBinding.inflate(inflater, container, false)

        adapter = EnvTypeRecyclerAdapter()
        binding.envTypeRecyclerview.adapter = adapter
        binding.envTypeRecyclerview.layoutManager = LinearLayoutManager(requireContext())

        val dividerItemDecoration = DividerItemDecoration(
            binding.envTypeRecyclerview.context,
            LinearLayoutManager.VERTICAL
        )
        binding.envTypeRecyclerview.addItemDecoration(dividerItemDecoration)

        envTypeViewModel.envTypeList.observe(viewLifecycleOwner) { envTypes ->
            adapter.setData(envTypes)
            envTypeList = envTypes
        }

        binding.addEnvtypeFloatButton.setOnClickListener {
            showAddDialog("New Environment Type", "Add new environment type?")
        }

        adapter.setOnItemClickListener(object: EnvTypeRecyclerAdapter.MyClickListener{
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
                insertEnvType(name)
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }
            .create().show()
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

    private fun showUpdateDialog(title: String, message: String, envType: EnvType){
        val input = EditText(requireContext())
        input.setText(envType.envTypeName)
        input.hint = "Name"
        input.inputType = InputType.TYPE_CLASS_TEXT

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(title)
            .setMessage(message)
            .setView(input)
            .setPositiveButton("OK") { _, _ ->
                val name = input.text.toString()
                updateEnvType(envType, name)
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }
            .create().show()
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
}