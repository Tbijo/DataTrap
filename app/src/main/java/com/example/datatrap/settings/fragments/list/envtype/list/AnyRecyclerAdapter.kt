package com.example.datatrap.settings.fragments.list.envtype.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.datatrap.databinding.SettingsRowBinding

class AnyRecyclerAdapter : RecyclerView.Adapter<AnyRecyclerAdapter.MyViewHolder>() {

    private var anyList = emptyList<Any>()

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
        val currenItem = anyList[position]
        holder.binding.tvSetName.text = currenItem.toString()
    }

    override fun getItemCount(): Int {
        return anyList.size
    }

    fun setData(any: List<Any>){
        this.anyList = any
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
