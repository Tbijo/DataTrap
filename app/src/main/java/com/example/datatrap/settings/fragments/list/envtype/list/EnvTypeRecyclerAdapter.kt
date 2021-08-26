package com.example.datatrap.settings.fragments.list.envtype.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.datatrap.databinding.SettingsRowBinding
import com.example.datatrap.models.EnvType

class EnvTypeRecyclerAdapter : RecyclerView.Adapter<EnvTypeRecyclerAdapter.MyViewHolder>() {

    private var envTypeList = emptyList<EnvType>()

    class MyViewHolder(val binding: SettingsRowBinding, listener: MyClickListener) : RecyclerView.ViewHolder(binding.root){
        init {
            binding.settingsRow.setOnClickListener {
                listener.useClickListener(adapterPosition)
            }

            binding.settingsRow.setOnLongClickListener {
                listener.useLongClickListener(adapterPosition)
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(SettingsRowBinding.inflate(LayoutInflater.from(parent.context), parent, false), mListener)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currenItem = envTypeList[position]
        holder.binding.tvSetName.text = currenItem.envTypeName
    }

    override fun getItemCount(): Int {
        return envTypeList.size
    }

    fun setData(envType: List<EnvType>){
        this.envTypeList = envType
        notifyDataSetChanged()
    }

    interface MyClickListener {
        fun useClickListener(position: Int)
        fun useLongClickListener(position: Int)
    }

    private lateinit var mListener: MyClickListener

    fun setOnItemClickListener(listener: MyClickListener){
        mListener = listener
    }

}
