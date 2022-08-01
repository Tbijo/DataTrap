package com.example.datatrap.occasion.fragments.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.datatrap.databinding.OccasionRowBinding
import com.example.datatrap.models.tuples.OccList
import java.text.SimpleDateFormat
import java.util.*

class OccasionRecyclerAdapter : RecyclerView.Adapter<OccasionRecyclerAdapter.MyViewHolder>() {

    private var occasionList = emptyList<OccList>()

    class MyViewHolder(val binding: OccasionRowBinding, listener: MyClickListener) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.occasionRow.setOnClickListener {
                listener.useClickListener(adapterPosition)
            }

            binding.occasionRow.setOnLongClickListener {
                listener.useLongClickListener(adapterPosition)
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(OccasionRowBinding.inflate(LayoutInflater.from(parent.context), parent, false), mListener)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currenItem = occasionList[position]
        holder.binding.tvOccasion.text = currenItem.occasion.toString()
        holder.binding.tvOccDate.text = SimpleDateFormat.getDateTimeInstance().format(Date(currenItem.occasionStart))
        holder.binding.tvNumMouse.text = currenItem.numMice.toString()
        holder.binding.tvNumTraps.text = currenItem.numTraps.toString()
    }

    override fun getItemCount(): Int {
        return occasionList.size
    }

    fun setData(occasions: List<OccList>){
        this.occasionList = occasions
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
