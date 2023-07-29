package com.example.datatrap.settings.protocol.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.datatrap.databinding.SettingsRowBinding
import com.example.datatrap.settings.protocol.data.Protocol

class ProtocolRecyclerAdapter : RecyclerView.Adapter<ProtocolRecyclerAdapter.MyViewHolder>() {

    private var protocolList = emptyList<Protocol>()

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
        val currenItem = protocolList[position]
        holder.binding.tvSetName.text = currenItem.protocolName
    }

    override fun getItemCount(): Int {
        return protocolList.size
    }

    fun setData(protList: List<Protocol>){
        this.protocolList = protList
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
