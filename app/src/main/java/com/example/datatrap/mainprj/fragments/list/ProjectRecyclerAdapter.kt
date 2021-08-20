package com.example.datatrap.mainprj.fragments.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.datatrap.databinding.ProjectRowBinding
import com.example.datatrap.models.Project

class ProjectRecyclerAdapter : RecyclerView.Adapter<ProjectRecyclerAdapter.MyViewHolder>() {

    private var projectList = emptyList<Project>()

    class MyViewHolder(val binding: ProjectRowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(ProjectRowBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currenItem = projectList[position]
        holder.binding.tvProjectName.text = currenItem.projectName
        holder.binding.tvNumLocality.text = currenItem.numLocal.toString()
        holder.binding.tvNumMice.text = currenItem.numMice.toString()
        holder.binding.tvProjectDate.text = currenItem.date

        holder.binding.projectRow.setOnClickListener {
            // tu sa prejde na locality s projektom ID
            // ROBI SA TO INAK TREBA PREROBIT LEBO IDEME DO DRUHEJ NAVIGACIE
            val bundle: Bundle = bundleOf("name" to "History")
            findNavController().setGraph(R.navigation.my_second_navigation, bundle)
        }

        holder.binding.projectRow.setOnLongClickListener {
            // tu sa prejde na upravu vybraneho projektu
            val action = ListAllProjectFragmentDirections.actionListAllProjectFragmentToUpdateProjectFragment(currenItem)
            holder.binding.root.findNavController().navigate(action)
            true
        }
    }

    override fun getItemCount(): Int {
        return projectList.size
    }

    fun setData(users: List<Project>){
        this.projectList = users
        notifyDataSetChanged()
    }
}