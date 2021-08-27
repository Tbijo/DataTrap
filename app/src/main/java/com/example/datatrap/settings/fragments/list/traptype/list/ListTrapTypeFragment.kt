package com.example.datatrap.settings.fragments.list.traptype.list

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
import com.example.datatrap.databinding.FragmentListTrapTypeBinding
import com.example.datatrap.models.TrapType
import com.example.datatrap.settings.fragments.list.envtype.list.AnyRecyclerAdapter
import com.example.datatrap.viewmodels.TrapTypeViewModel

class ListTrapTypeFragment : Fragment() {

    private var _binding: FragmentListTrapTypeBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: AnyRecyclerAdapter
    private lateinit var trapTypeViewModel: TrapTypeViewModel
    private lateinit var trapTypeList: List<TrapType>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        _binding = FragmentListTrapTypeBinding.inflate(inflater, container, false)
        trapTypeViewModel = ViewModelProvider(this).get(TrapTypeViewModel::class.java)

        adapter = AnyRecyclerAdapter()
        binding.trapTypeRecyclerview.adapter = adapter
        binding.trapTypeRecyclerview.layoutManager = LinearLayoutManager(requireContext())

        trapTypeViewModel.trapTypeList.observe(viewLifecycleOwner, Observer { trapTypes ->
            adapter.setData(trapTypes)
            trapTypeList = trapTypes
        })

        binding.addTraptypeFloatButton.setOnClickListener {
            showAddDialog("New Trap Type", "Add new trap type?")
        }

        adapter.setOnItemClickListener(object: AnyRecyclerAdapter.MyClickListener{
            override fun useClickListener(position: Int) {
                // update trap Type
                val currName: String = trapTypeList[position].trapTypeName
                showUpdateDialog("Update Trap Type", "Update trap type?", currName)
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

            val trapType: TrapType = TrapType(name)

            trapTypeViewModel.insertTrapType(trapType)

            Toast.makeText(requireContext(),"New trap type added.", Toast.LENGTH_SHORT).show()
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
                updateTrapType(name)
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }
            .create().show()
    }

    private fun updateTrapType(name: String){
        if (name.isNotEmpty()){

            val trapType: TrapType = TrapType(name)

            trapTypeViewModel.updateTrapType(trapType)

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