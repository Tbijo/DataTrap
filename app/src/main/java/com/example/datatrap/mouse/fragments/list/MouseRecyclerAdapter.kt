package com.example.datatrap.mouse.fragments.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.datatrap.databinding.MouseRowBinding
import com.example.datatrap.models.tuples.MouseOccList
import java.text.SimpleDateFormat
import java.util.*

class MouseRecyclerAdapter : RecyclerView.Adapter<MouseRecyclerAdapter.MyViewHolder>() {

    private var mouseList = emptyList<MouseOccList>()

    class MyViewHolder(val binding: MouseRowBinding, listener: MyClickListener) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.mouseRow.setOnClickListener {
                listener.useClickListener(adapterPosition)
            }

            binding.mouseRow.setOnLongClickListener {
                listener.useLongClickListener(adapterPosition)
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(MouseRowBinding.inflate(LayoutInflater.from(parent.context), parent, false), mListener)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currenItem = mouseList[position]

        holder.binding.tvIdIndividual.text = currenItem.mouseCode.toString()

        holder.binding.tvMouseSpecieCode.text = currenItem.specieCode

        val dateFormated = SimpleDateFormat.getDateTimeInstance().format(Date(currenItem.mouseCaught))
        holder.binding.tvCatchDateTime.text = dateFormated

    }

    override fun getItemCount(): Int {
        return mouseList.size
    }

    fun setData(mice: List<MouseOccList>) {
        this.mouseList = mice
        notifyDataSetChanged()
    }

    interface MyClickListener {
        fun useClickListener(position: Int)
        fun useLongClickListener(position: Int)
    }

    private lateinit var mListener: MyClickListener

    fun setOnItemClickListener(listener: MyClickListener) {
        mListener = listener
    }

}
