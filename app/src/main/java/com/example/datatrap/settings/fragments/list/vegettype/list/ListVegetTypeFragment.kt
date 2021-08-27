package com.example.datatrap.settings.fragments.list.vegettype.list

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
import com.example.datatrap.databinding.FragmentListVegetTypeBinding
import com.example.datatrap.models.VegetType
import com.example.datatrap.settings.fragments.list.envtype.list.AnyRecyclerAdapter
import com.example.datatrap.viewmodels.VegetTypeViewModel

class ListVegetTypeFragment : Fragment() {

    private var _binding: FragmentListVegetTypeBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: AnyRecyclerAdapter
    private lateinit var vegTypeViewModel: VegetTypeViewModel
    private lateinit var vegTypeList: List<VegetType>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        _binding = FragmentListVegetTypeBinding.inflate(inflater, container, false)
        vegTypeViewModel = ViewModelProvider(this).get(VegetTypeViewModel::class.java)

        adapter = AnyRecyclerAdapter()
        binding.vegTypeRecyclerview.adapter = adapter
        binding.vegTypeRecyclerview.layoutManager = LinearLayoutManager(requireContext())

        vegTypeViewModel.vegetTypeList.observe(viewLifecycleOwner, Observer { vegTypes ->
            adapter.setData(vegTypes)
            vegTypeList = vegTypes
        })

        binding.addVegtypeFloatButton.setOnClickListener {
            showAddDialog("New Vegetation Type", "Add new vegetation type?")
        }

        adapter.setOnItemClickListener(object: AnyRecyclerAdapter.MyClickListener{
            override fun useClickListener(position: Int) {
                // update veg Type
                val currName: String = vegTypeList[position].vegetTypeName
                showUpdateDialog("Update Vegetation Type", "Update vegetation type?", currName)
            }

            override fun useLongClickListener(position: Int) {
                // delete veg Type
                val vegType: VegetType = vegTypeList[position]
                deleteEnvType(vegType, "Vegetation type", "Vegetation Type")
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
                insertVegType(name)
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }
            .create().show()
    }

    private fun insertVegType(name: String){
        if (name.isNotEmpty()){

            val vegType: VegetType = VegetType(name)

            vegTypeViewModel.insertVegetType(vegType)

            Toast.makeText(requireContext(),"New vegetation type added.", Toast.LENGTH_SHORT).show()
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
                updateVegType(name)
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }
            .create().show()
    }

    private fun updateVegType(name: String){
        if (name.isNotEmpty()){

            val vegType: VegetType = VegetType(name)

            vegTypeViewModel.updateVegetType(vegType)

            Toast.makeText(requireContext(),"Vegetation type updated.", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(requireContext(), getString(R.string.emptyFields), Toast.LENGTH_LONG).show()
        }
    }

    private fun deleteEnvType(vegType: VegetType, what: String, title: String) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes"){_, _ ->

            vegTypeViewModel.deleteVegetType(vegType)
            Toast.makeText(requireContext(),"$what deleted.", Toast.LENGTH_LONG).show()
        }
            .setNegativeButton("No"){_, _ -> }
            .setTitle("Delete $title")
            .setMessage("Are you sure you want to delete this $what ?")
            .create().show()
    }

}