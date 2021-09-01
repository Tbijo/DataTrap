package com.example.datatrap.mouse.fragments.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.datatrap.databinding.MouseRowBinding
import com.example.datatrap.models.Mouse
import com.example.datatrap.models.Specie

class MouseRecyclerAdapter : RecyclerView.Adapter<MouseRecyclerAdapter.MyViewHolder>() {

    private var mouseList = emptyList<Mouse>()
    private var specieList = emptyList<Specie>()

    class MyViewHolder(val binding: MouseRowBinding, listener: MyClickListener) : RecyclerView.ViewHolder(binding.root){
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
        holder.binding.tvIdIndividual.text = currenItem.code.toString()

        /*co je lepsie skakat do databazy a postupne vytahovat specie code*/
        /*alebo si sem dat list vsetkych a vytahovat z neho takto*/
        specieList.forEach {
            if (it.specieId == currenItem.speciesID){
                holder.binding.tvMouseSpecieCode.text = it.speciesCode
            }
        }

        holder.binding.tvCatchDateTime.text = "${currenItem.date} ${currenItem.catchTime}"

    }

    override fun getItemCount(): Int {
        return mouseList.size
    }

    fun setData(mice: List<Mouse>, species: List<Specie>){
        this.mouseList = mice
        this.specieList = species
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
