package com.example.datatrap.mouse.fragments.listinview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.datatrap.databinding.MouseHistRowBinding

class MouseHistRecyclerAdapter : RecyclerView.Adapter<MouseHistRecyclerAdapter.MyViewHolder>()  {

    private var historyList = emptyList<String>()

    class MyViewHolder(val binding: MouseHistRowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(MouseHistRowBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = historyList[position]
        holder.binding.tvLog.text = currentItem[position].toString()
    }

    override fun getItemCount(): Int {
        return historyList.size
    }

    fun setData(historyList: List<String>){
        this.historyList = historyList
        notifyDataSetChanged()
    }

}
