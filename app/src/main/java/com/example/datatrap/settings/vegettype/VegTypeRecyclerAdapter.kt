package com.example.datatrap.settings.vegettype.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.datatrap.databinding.SettingsRowBinding
import com.example.datatrap.settings.vegettype.data.VegetType

class VegTypeRecyclerAdapter : RecyclerView.Adapter<VegTypeRecyclerAdapter.MyViewHolder>() {

    private var vegTypeList = emptyList<VegetType>()

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
        val currenItem = vegTypeList[position]
        holder.binding.tvSetName.text = currenItem.vegetTypeName
    }

    override fun getItemCount(): Int {
        return vegTypeList.size
    }

    fun setData(vegTypeList: List<VegetType>){
        this.vegTypeList = vegTypeList
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
