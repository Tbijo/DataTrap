package com.example.datatrap.settings.fragments.list.envtype.list

import android.text.InputType
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.example.datatrap.R
import com.example.datatrap.databinding.SettingsRowBinding
import com.example.datatrap.models.EnvType

class EnvTypeRecyclerAdapter : RecyclerView.Adapter<EnvTypeRecyclerAdapter.MyViewHolder>() {

    private var envTypeList = emptyList<EnvType>()

    class MyViewHolder(val binding: SettingsRowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(SettingsRowBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currenItem = envTypeList[position]
        holder.binding.tvSetName.text = currenItem.envTypeName

        holder.binding.settingsRow.setOnClickListener {
            // update vybraneho envtype
            showUpdateDialog("","","")
        }

        holder.binding.settingsRow.setOnLongClickListener {
            // odstranenie vybraneho envtype
            deleteEnvType(currenItem)
            true
        }
    }

    private fun deleteEnvType(currenItem: EnvType) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        return envTypeList.size
    }

    fun setData(envType: List<EnvType>){
        this.envTypeList = envType
        notifyDataSetChanged()
    }

    private fun showUpdateDialog(title: String, hint: String, message: String){
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

}
