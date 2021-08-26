package com.example.datatrap.settings.fragments.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.datatrap.databinding.SettingsRowBinding

class SettingsRecyclerAdapter : RecyclerView.Adapter<SettingsRecyclerAdapter.MyViewHolder>() {

    private var settingsList = listOf("Environment Type", "Method", "Method Type", "Protocol", "Trap Type", "Vegetation Type")

    class MyViewHolder(val binding: SettingsRowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(SettingsRowBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currenItem = settingsList[position]
        holder.binding.tvSetName.text = currenItem[position].toString()

        val actionEnType = ListMainFragmentDirections.actionListMainFragmentToListEnvTypeFragment()
        val actionMethod = ListMainFragmentDirections.actionListMainFragmentToListMethodFragment()
        val actionMetType = ListMainFragmentDirections.actionListMainFragmentToListMethodTypeFragment()
        val actionProt = ListMainFragmentDirections.actionListMainFragmentToListProtocolFragment()
        val actionTraType = ListMainFragmentDirections.actionListMainFragmentToListTrapTypeFragment()
        val actionVegType = ListMainFragmentDirections.actionListMainFragmentToListVegetTypeFragment()

        holder.binding.settingsRow.setOnClickListener {
            when(currenItem){
                "Environment Type" -> holder.binding.settingsRow.findNavController().navigate(actionEnType)
                "Method" -> holder.binding.settingsRow.findNavController().navigate(actionMethod)
                "Method Type" -> holder.binding.settingsRow.findNavController().navigate(actionMetType)
                "Protocol" -> holder.binding.settingsRow.findNavController().navigate(actionProt)
                "Trap Type" -> holder.binding.settingsRow.findNavController().navigate(actionTraType)
                "Vegetation Type" -> holder.binding.settingsRow.findNavController().navigate(actionVegType)
            }
        }
    }

    override fun getItemCount(): Int {
        return settingsList.size
    }

}
