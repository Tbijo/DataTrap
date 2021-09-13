package com.example.datatrap.mouse.fragments.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.datatrap.databinding.MouseRowBinding
import com.example.datatrap.models.Mouse
import com.example.datatrap.viewmodels.SpecieViewModel

class MouseRecyclerAdapter(owner: ViewModelStoreOwner) : RecyclerView.Adapter<MouseRecyclerAdapter.MyViewHolder>() {

    private var mouseList = emptyList<Mouse>()
    private val specieViewModel: SpecieViewModel = ViewModelProvider(owner).get(SpecieViewModel::class.java)

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

        holder.binding.tvMouseSpecieCode.text = specieViewModel.getSpecie(currenItem.speciesID).value?.speciesCode

        holder.binding.tvCatchDateTime.text = "${currenItem.catchDateTime}"
    }

    override fun getItemCount(): Int {
        return mouseList.size
    }

    fun setData(mice: List<Mouse>){
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
