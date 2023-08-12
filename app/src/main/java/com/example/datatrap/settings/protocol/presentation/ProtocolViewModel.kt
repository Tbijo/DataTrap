package com.example.datatrap.settings.protocol.presentation

import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datatrap.R
import com.example.datatrap.settings.protocol.data.ProtocolEntity
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

    val procolList: LiveData<List<ProtocolEntity>> = protocolRepository.protocolEntityList
    private lateinit var protocolEntityList: List<ProtocolEntity>


    fun insertProtocol(protocolEntity: ProtocolEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            protocolRepository.insertProtocol(protocolEntity)
        }
    }

    fun updateProtocol(protocolEntity: ProtocolEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            protocolRepository.updateProtocol(protocolEntity)
        }
    }

    fun deleteProtocol(protocolEntity: ProtocolEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            protocolRepository.deleteProtocol(protocolEntity)
        }
    }

    init {
        protocolViewModel.procolList.observe(viewLifecycleOwner) { protocols ->
            adapter.setData(protocols)
            protocolEntityList = protocols
        }

        binding.addProtocolFloatButton.setOnClickListener {
            showAddDialog("New Protocol", "Add new protocol?")
        }

        adapter.setOnItemClickListener(object: ProtocolRecyclerAdapter.MyClickListener {
            override fun useClickListener(position: Int) {
                // update protocol
                val currProtocolEntity: ProtocolEntity = protocolEntityList[position]
                showUpdateDialog("Update Protocol", "Update protocol?", currProtocolEntity)
            }

            override fun useLongClickListener(position: Int) {
                // delete protocol
                val protocolEntity: ProtocolEntity = protocolEntityList[position]

                deleteEnvType(protocolEntity, "Protocol", "Protocol")
            }
        })
    }

    private fun insertProtocol(name: String){
        if (name.isNotEmpty()){

            val protocolEntity: ProtocolEntity = ProtocolEntity(0, name, Calendar.getInstance().time, null)

            protocolViewModel.insertProtocol(protocolEntity)

            Toast.makeText(requireContext(),"New protocol added.", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(requireContext(), getString(R.string.emptyFields), Toast.LENGTH_LONG).show()
        }
    }

    private fun updateProtocol(prot: ProtocolEntity, name: String){
        if (name.isNotEmpty()){

            val protocolEntityNew: ProtocolEntity = prot
            protocolEntityNew.protocolName = name
            protocolEntityNew.protDateTimeUpdated = Calendar.getInstance().time

            protocolViewModel.updateProtocol(protocolEntityNew)

            Toast.makeText(requireContext(),"Protocol updated.", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(requireContext(), getString(R.string.emptyFields), Toast.LENGTH_LONG).show()
        }
    }

    private fun deleteEnvType(protocolEntity: ProtocolEntity, what: String, title: String) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes"){_, _ ->

            protocolViewModel.deleteProtocol(protocolEntity)
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