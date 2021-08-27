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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.datatrap.R
import com.example.datatrap.databinding.FragmentListEnvTypeBinding
import com.example.datatrap.models.EnvType
import com.example.datatrap.project.fragments.UpdateProjectFragmentDirections
import com.example.datatrap.viewmodels.EnvTypeViewModel

class ListEnvTypeFragment : Fragment() {

    private var _binding: FragmentListEnvTypeBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: EnvTypeRecyclerAdapter
    private lateinit var envTypeViewModel: EnvTypeViewModel
    private lateinit var envTypeList: List<EnvType>

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
            envTypeList = envTypes
        })

        binding.addEnvtypeFloatButton.setOnClickListener {
            showAddDialog("New Environment Type", "Name", "Add new environment type?")
        }

        adapter.setOnItemClickListener(object: EnvTypeRecyclerAdapter.MyClickListener{
            override fun useClickListener(position: Int) {
                // update envType
                val currName: String = envTypeList[position].envTypeName
                showUpdateDialog("Update Environment Type", "Name", "Update environment type?", currName)
            }

            override fun useLongClickListener(position: Int) {
                // delete envType
                val envType: EnvType = envTypeList[position]
                deleteEnvType(envType)
            }
        })

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

    private fun showUpdateDialog(title: String, hint: String, message: String, currName: String){
        val input = EditText(requireContext())
        input.setText(currName)
        input.hint = hint
        input.inputType = InputType.TYPE_CLASS_TEXT

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(title)
            .setMessage(message)
            .setView(input)
            .setPositiveButton("OK") { _, _ ->
                val name = input.text.toString()
                updateEnvType(name)
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }
            .create().show()
    }

    private fun updateEnvType(name: String){
        if (name.isNotEmpty()){

            val envType: EnvType = EnvType(name)

            envTypeViewModel.insertEnvType(envType)

            Toast.makeText(requireContext(),"Environment type updated.", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(requireContext(), getString(R.string.emptyFields), Toast.LENGTH_LONG).show()
        }
    }

    private fun deleteEnvType(envType: EnvType) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes"){_, _ ->

            envTypeViewModel.deleteEnvType(envType)

            Toast.makeText(requireContext(),"Environment type deleted.", Toast.LENGTH_LONG).show()
            val action = UpdateProjectFragmentDirections.actionUpdateProjectFragmentToListAllProjectFragment()
            findNavController().navigate(action)
        }
            .setNegativeButton("No"){_, _ -> }
            .setTitle("Delete Environment type?")
            .setMessage("Are you sure you want to delete this Environment type?")
            .create().show()
    }
}