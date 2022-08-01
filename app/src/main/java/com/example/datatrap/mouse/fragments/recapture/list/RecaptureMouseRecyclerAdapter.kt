package com.example.datatrap.mouse.fragments.recapture.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.datatrap.databinding.RecaptureRowBinding
import com.example.datatrap.models.tuples.MouseRecapList
import java.text.SimpleDateFormat
import java.util.*

class RecaptureMouseRecyclerAdapter : RecyclerView.Adapter<RecaptureMouseRecyclerAdapter.MyViewHolder>() {

    private var mouseList = emptyList<MouseRecapList>()

    class MyViewHolder(val binding: RecaptureRowBinding, listener: MyClickListener) : RecyclerView.ViewHolder(binding.root){
        init {
            binding.recaptureRow.setOnClickListener {
                listener.useClickListener(adapterPosition)
            }

            binding.recaptureRow.setOnLongClickListener {
                listener.useLongClickListener(adapterPosition)
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(RecaptureRowBinding.inflate(LayoutInflater.from(parent.context), parent, false), mListener)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currenItem = mouseList[position]

        holder.binding.tvMouseCode.text = currenItem.code.toString()

        holder.binding.tvAge.text = currenItem.age.toString()

        holder.binding.tvWeight.text = currenItem.weight.toString()

        holder.binding.tvSex.text = currenItem.sex.toString()

        holder.binding.tvGravitRecap.text = when (currenItem.gravidity) {
            true -> "Yes"
            false -> "No"
            else -> "null"
        }

        holder.binding.tvLactaRecap.text = when (currenItem.lactating) {
            true -> "Yes"
            false -> "No"
            else -> "null"
        }

        holder.binding.tvSexActiveRecap.text = if (currenItem.sexActive) "Yes" else "No"

        holder.binding.tvLocality.text = currenItem.localityName

        holder.binding.tvSpecieRecap.text = currenItem.specieCode

        val dateFormated = SimpleDateFormat.getDateTimeInstance().format(Date(currenItem.mouseCaught))
        holder.binding.tvRecapDate.text = dateFormated
    }

    override fun getItemCount(): Int {
        return mouseList.size
    }

    fun setData(mice: List<MouseRecapList>){
        this.mouseList = mice
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
