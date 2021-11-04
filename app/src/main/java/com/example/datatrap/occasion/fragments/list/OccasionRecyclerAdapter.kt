package com.example.datatrap.occasion.fragments.list

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.datatrap.databinding.OccasionRowBinding
import com.example.datatrap.models.Occasion
import java.text.SimpleDateFormat

class OccasionRecyclerAdapter : RecyclerView.Adapter<OccasionRecyclerAdapter.MyViewHolder>() {

    private var occasionList = emptyList<Occasion>()

    class MyViewHolder(val binding: OccasionRowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(OccasionRowBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currenItem = occasionList[position]
        holder.binding.tvOccasion.text = currenItem.occasion.toString()
        holder.binding.tvOccDate.text = SimpleDateFormat.getDateTimeInstance().format(currenItem.occasionDateTimeCreated)
        holder.binding.tvNumMouse.text = currenItem.numMice.toString()
        holder.binding.tvNumTraps.text = currenItem.numTraps.toString()

        holder.binding.occasionRow.setOnClickListener {
            // tu sa pojde do Mouse s occasion
            val action = ListSesOccasionFragmentDirections.actionListSesOccasionFragmentToListOccMouseFragment(currenItem)
            holder.binding.root.findNavController().navigate(action)
        }

        holder.binding.occasionRow.setOnLongClickListener {
            // tu sa pojde do update occasion
            val action = ListSesOccasionFragmentDirections.actionListSesOccasionFragmentToUpdateOccasionFragment(currenItem)
            holder.binding.root.findNavController().navigate(action)
            true
        }
    }

    override fun getItemCount(): Int {
        return occasionList.size
    }

    fun getListSize(): Int{
        return occasionList.size
    }

    fun setData(occasions: List<Occasion>){
        this.occasionList = occasions
        notifyDataSetChanged()
    }

}
