package com.example.datatrap.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.datatrap.core.presentation.components.MyScaffold

@Composable
fun SettingsListScreen() {
    MyScaffold(title = "Settings") {
        Column(modifier = Modifier.padding(it)) {

            var settingsList = listOf("Environment Type", "Method", "Method Type", "Protocol", "Trap Type", "Vegetation Type", "User")

            when(settingsList){
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
}