package com.example.datatrap.settings.fragments.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.datatrap.databinding.SettingsRowBinding

class SettingsRecyclerAdapter : RecyclerView.Adapter<SettingsRecyclerAdapter.MyViewHolder>() {

    private var settingsList = listOf("Environment Type", "Method", "Method Type", "Protocol", "Trap Type", "Vegetation Type", "User")

    class MyViewHolder(val binding: SettingsRowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(SettingsRowBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currenItem: String = settingsList[position]
        holder.binding.tvSetName.text = currenItem

        holder.binding.root.setOnClickListener {
            when(currenItem){
                "Environment Type" -> {
                    val actionEnType = ListMainFragmentDirections.actionListMainFragmentToListEnvTypeFragment()
                    holder.binding.settingsRow.findNavController().navigate(actionEnType)
                }
                "Method" -> {
                    val actionMethod = ListMainFragmentDirections.actionListMainFragmentToListMethodFragment()
                    holder.binding.settingsRow.findNavController().navigate(actionMethod)
                }
                "Method Type" -> {
                    val actionMetType = ListMainFragmentDirections.actionListMainFragmentToListMethodTypeFragment()
                    holder.binding.settingsRow.findNavController().navigate(actionMetType)
                }
                "Protocol" -> {
                    val actionProt = ListMainFragmentDirections.actionListMainFragmentToListProtocolFragment()
                    holder.binding.settingsRow.findNavController().navigate(actionProt)
                }
                "Trap Type" -> {
                    val actionTraType = ListMainFragmentDirections.actionListMainFragmentToListTrapTypeFragment()
                    holder.binding.settingsRow.findNavController().navigate(actionTraType)
                }
                "Vegetation Type" -> {
                    val actionVegType = ListMainFragmentDirections.actionListMainFragmentToListVegetTypeFragment()
                    holder.binding.settingsRow.findNavController().navigate(actionVegType)
                }
                "User" -> {
                    val actionUser = ListMainFragmentDirections.actionListMainFragmentToListUsersFragment()
                    holder.binding.settingsRow.findNavController().navigate(actionUser)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return 7
    }

}
