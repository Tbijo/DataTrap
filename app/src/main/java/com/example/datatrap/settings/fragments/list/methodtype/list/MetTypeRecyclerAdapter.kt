package com.example.datatrap.settings.fragments.list.methodtype.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.datatrap.databinding.SettingsRowBinding
import com.example.datatrap.models.MethodType

class MetTypeRecyclerAdapter : RecyclerView.Adapter<MetTypeRecyclerAdapter.MyViewHolder>() {

    private var methodTypeList = emptyList<MethodType>()

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
        val currenItem = methodTypeList[position]
        holder.binding.tvSetName.text = currenItem.methodTypeName
    }

    override fun getItemCount(): Int {
        return methodTypeList.size
    }

    fun setData(methodTypeList: List<MethodType>){
        this.methodTypeList = methodTypeList
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
