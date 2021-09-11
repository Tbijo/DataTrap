package com.example.datatrap.locality.fragments.list.prj

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.datatrap.databinding.LocalityRowBinding
import com.example.datatrap.models.Locality
import java.text.SimpleDateFormat

class PrjLocalityRecyclerAdapter : RecyclerView.Adapter<PrjLocalityRecyclerAdapter.MyViewHolder>() {

    private var localityList = emptyList<Locality>()

    class MyViewHolder(val binding: LocalityRowBinding, listener: MyClickListener) : RecyclerView.ViewHolder(binding.root){
        init {
            binding.localityRow.setOnClickListener {
                listener.useClickListener(adapterPosition)
            }

            binding.localityRow.setOnLongClickListener {
                listener.useLongClickListener(adapterPosition)
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LocalityRowBinding.inflate(LayoutInflater.from(parent.context), parent, false), mListener)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currenItem = localityList[position]
        holder.binding.tvName.text = currenItem.localityName
        holder.binding.tvDate.text = SimpleDateFormat.getDateTimeInstance().format(currenItem.localityDateTime)
        holder.binding.tvNumSes.text = currenItem.numSessions.toString()
    }

    override fun getItemCount(): Int {
        return localityList.size
    }

    fun setData(localities: List<Locality>){
        this.localityList = localities
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