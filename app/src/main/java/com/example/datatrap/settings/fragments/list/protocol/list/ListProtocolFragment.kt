package com.example.datatrap.settings.fragments.list.protocol.list

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
import com.example.datatrap.databinding.FragmentListProtocolBinding
import com.example.datatrap.models.Protocol
import com.example.datatrap.settings.fragments.list.envtype.list.EnvTypeRecyclerAdapter
import com.example.datatrap.viewmodels.ProtocolViewModel
import java.util.*

class ListProtocolFragment : Fragment() {

    private var _binding: FragmentListProtocolBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ProtocolRecyclerAdapter
    private lateinit var protocolViewModel: ProtocolViewModel
    private lateinit var protocolList: List<Protocol>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        _binding = FragmentListProtocolBinding.inflate(inflater, container, false)
        protocolViewModel = ViewModelProvider(this).get(ProtocolViewModel::class.java)

        adapter = ProtocolRecyclerAdapter()
        binding.protocolRecyclerview.adapter = adapter
        binding.protocolRecyclerview.layoutManager = LinearLayoutManager(requireContext())

        protocolViewModel.procolList.observe(viewLifecycleOwner, Observer { protocols ->
            adapter.setData(protocols)
            protocolList = protocols
        })

        binding.addProtocolFloatButton.setOnClickListener {
            showAddDialog("New Protocol", "Add new protocol?")
        }

        adapter.setOnItemClickListener(object: ProtocolRecyclerAdapter.MyClickListener{
            override fun useClickListener(position: Int) {
                // update protocol
                val currProtocol: Protocol = protocolList[position]
                showUpdateDialog("Update Protocol", "Update protocol?", currProtocol)
            }

            override fun useLongClickListener(position: Int) {
                // delete protocol
                val protocol: Protocol = protocolList[position]

                deleteEnvType(protocol, "Protocol", "Protocol")
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
                insertProtocol(name)
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }
            .create().show()
    }

    private fun insertProtocol(name: String){
        if (name.isNotEmpty()){

            val protocol: Protocol = Protocol(0, name, Calendar.getInstance().time, null)

            protocolViewModel.insertProtocol(protocol)

            Toast.makeText(requireContext(),"New protocol added.", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(requireContext(), getString(R.string.emptyFields), Toast.LENGTH_LONG).show()
        }
    }

    private fun showUpdateDialog(title: String, message: String, prot: Protocol){
        val input = EditText(requireContext())
        input.setText(prot.protocolName)
        input.hint = "Name"
        input.inputType = InputType.TYPE_CLASS_TEXT

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(title)
            .setMessage(message)
            .setView(input)
            .setPositiveButton("OK") { _, _ ->
                val name = input.text.toString()
                updateProtocol(prot, name)
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }
            .create().show()
    }

    private fun updateProtocol(prot: Protocol, name: String){
        if (name.isNotEmpty()){

            val protocolNew: Protocol = prot
            protocolNew.protocolName = name
            protocolNew.protDateTimeUpdated = Calendar.getInstance().time

            protocolViewModel.updateProtocol(protocolNew)

            Toast.makeText(requireContext(),"Protocol updated.", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(requireContext(), getString(R.string.emptyFields), Toast.LENGTH_LONG).show()
        }
    }

    private fun deleteEnvType(protocol: Protocol, what: String, title: String) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes"){_, _ ->

            protocolViewModel.deleteProtocol(protocol)
            Toast.makeText(requireContext(),"$what deleted.", Toast.LENGTH_LONG).show()
        }
            .setNegativeButton("No"){_, _ -> }
            .setTitle("Delete $title")
            .setMessage("Are you sure you want to delete this $what ?")
            .create().show()
    }

}