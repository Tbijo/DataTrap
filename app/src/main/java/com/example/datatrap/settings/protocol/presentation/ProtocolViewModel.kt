package com.example.datatrap.settings.protocol.presentation

import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datatrap.R
import com.example.datatrap.settings.protocol.data.Protocol
import com.example.datatrap.settings.protocol.data.ProtocolRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class ProtocolViewModel @Inject constructor(
    private val protocolRepository: ProtocolRepository
): ViewModel() {

    val procolList: LiveData<List<Protocol>> = protocolRepository.protocolList
    private lateinit var protocolList: List<Protocol>


    fun insertProtocol(protocol: Protocol) {
        viewModelScope.launch(Dispatchers.IO) {
            protocolRepository.insertProtocol(protocol)
        }
    }

    fun updateProtocol(protocol: Protocol) {
        viewModelScope.launch(Dispatchers.IO) {
            protocolRepository.updateProtocol(protocol)
        }
    }

    fun deleteProtocol(protocol: Protocol) {
        viewModelScope.launch(Dispatchers.IO) {
            protocolRepository.deleteProtocol(protocol)
        }
    }

    init {
        protocolViewModel.procolList.observe(viewLifecycleOwner) { protocols ->
            adapter.setData(protocols)
            protocolList = protocols
        }

        binding.addProtocolFloatButton.setOnClickListener {
            showAddDialog("New Protocol", "Add new protocol?")
        }

        adapter.setOnItemClickListener(object: ProtocolRecyclerAdapter.MyClickListener {
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

    fun onEvent(event: ProtocolListScreenEvent) {
        when(event) {
            is ProtocolListScreenEvent.OnDeleteClick -> TODO()
            is ProtocolListScreenEvent.OnInsertClick -> TODO()
            is ProtocolListScreenEvent.OnItemClick -> TODO()
        }
    }
}