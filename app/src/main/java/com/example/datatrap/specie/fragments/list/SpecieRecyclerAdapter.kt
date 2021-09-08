package com.example.datatrap.specie.fragments.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.datatrap.databinding.SpecieRowBinding
import com.example.datatrap.models.Specie

class SpecieRecyclerAdapter : RecyclerView.Adapter<SpecieRecyclerAdapter.MyViewHolder>() {

    private var specieList = emptyList<Specie>()

    class MyViewHolder(val binding: SpecieRowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(SpecieRowBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currenItem = specieList[position]
        holder.binding.tvCode.text = currenItem.speciesCode
        holder.binding.tvLatin.text = currenItem.fullName

        holder.binding.specieRow.setOnClickListener {
            // tu sa pojde pozriet specie
            val action = ListSpecieFragmentDirections.actionListSpecieFragmentToViewSpecieFragment(currenItem)
            holder.binding.root.findNavController().navigate(action)
        }

        holder.binding.specieRow.setOnLongClickListener {
            // tu sa pojde updatnut specie
            val action = ListSpecieFragmentDirections.actionListSpecieFragmentToUpdateSpecieFragment(currenItem)
            holder.binding.root.findNavController().navigate(action)
            true
        }
    }

    override fun getItemCount(): Int {
        return specieList.size
    }

    fun setData(species: List<Specie>){
        this.specieList = species
        notifyDataSetChanged()
    }

}
