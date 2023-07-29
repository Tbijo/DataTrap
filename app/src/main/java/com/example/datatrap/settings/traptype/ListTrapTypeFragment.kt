package com.example.datatrap.settings.traptype.list

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
import com.example.datatrap.databinding.FragmentListTrapTypeBinding
import com.example.datatrap.settings.traptype.data.TrapType
import com.example.datatrap.settings.traptype.presentation.TrapTypeViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class ListTrapTypeFragment : Fragment() {

    private var _binding: FragmentListTrapTypeBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: TrapTypeRecyclerAdapter
    private val trapTypeViewModel: TrapTypeViewModel by viewModels()
    private lateinit var trapTypeList: List<TrapType>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        _binding = FragmentListTrapTypeBinding.inflate(inflater, container, false)

        adapter = TrapTypeRecyclerAdapter()
        binding.trapTypeRecyclerview.adapter = adapter
        binding.trapTypeRecyclerview.layoutManager = LinearLayoutManager(requireContext())

        val dividerItemDecoration = DividerItemDecoration(
            binding.trapTypeRecyclerview.context,
            LinearLayoutManager.VERTICAL
        )
        binding.trapTypeRecyclerview.addItemDecoration(dividerItemDecoration)

        trapTypeViewModel.trapTypeList.observe(viewLifecycleOwner) { trapTypes ->
            adapter.setData(trapTypes)
            trapTypeList = trapTypes
        }

        binding.addTraptypeFloatButton.setOnClickListener {
            showAddDialog("New Trap Type", "Add new trap type?")
        }

        adapter.setOnItemClickListener(object: TrapTypeRecyclerAdapter.MyClickListener {
            override fun useClickListener(position: Int) {
                // update trap Type
                val currTrapType: TrapType = trapTypeList[position]
                showUpdateDialog("Update Trap Type", "Update trap type?", currTrapType)
            }

            override fun useLongClickListener(position: Int) {
                // delete trap Type
                val trapType: TrapType = trapTypeList[position]

                deleteEnvType(trapType, "Trap type", "Trap Type")
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
                insertTrapType(name)
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }
            .create().show()
    }

    private fun insertTrapType(name: String){
        if (name.isNotEmpty()){

            val trapType: TrapType = TrapType(0, name, Calendar.getInstance().time, null)

            trapTypeViewModel.insertTrapType(trapType)

            Toast.makeText(requireContext(),"New trap type added.", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(requireContext(), getString(R.string.emptyFields), Toast.LENGTH_LONG).show()
        }
    }

    private fun showUpdateDialog(title: String, message: String, trapType: TrapType){
        val input = EditText(requireContext())
        input.setText(trapType.trapTypeName)
        input.hint = "Name"
        input.inputType = InputType.TYPE_CLASS_TEXT

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(title)
            .setMessage(message)
            .setView(input)
            .setPositiveButton("OK") { _, _ ->
                val name = input.text.toString()
                updateTrapType(trapType, name)
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }
            .create().show()
    }

    private fun updateTrapType(trapType: TrapType, name: String){
        if (name.isNotEmpty()){

            val trapTypeNew: TrapType = trapType
            trapTypeNew.trapTypeName = name
            trapTypeNew.trapTypeDateTimeUpdated = Calendar.getInstance().time

            trapTypeViewModel.updateTrapType(trapTypeNew)

            Toast.makeText(requireContext(),"Trap type updated.", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(requireContext(), getString(R.string.emptyFields), Toast.LENGTH_LONG).show()
        }
    }

    private fun deleteEnvType(trapType: TrapType, what: String, title: String) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes"){_, _ ->

            trapTypeViewModel.deleteTrapType(trapType)
            Toast.makeText(requireContext(),"$what deleted.", Toast.LENGTH_LONG).show()
        }
            .setNegativeButton("No"){_, _ -> }
            .setTitle("Delete $title")
            .setMessage("Are you sure you want to delete this $what ?")
            .create().show()
    }

}