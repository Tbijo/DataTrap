package com.example.datatrap.project.presentation.project_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.datatrap.databinding.ProjectRowBinding
import com.example.datatrap.project.data.Project
import java.text.SimpleDateFormat
import java.util.*

class ProjectRecyclerAdapter : RecyclerView.Adapter<ProjectRecyclerAdapter.MyViewHolder>() {

    private var projectList = emptyList<Project>()

    class MyViewHolder(val binding: ProjectRowBinding, listener: MyClickListener) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.projectRow.setOnClickListener {
                listener.useClickListener(adapterPosition)
            }

            binding.projectRow.setOnLongClickListener {
                listener.useLongClickListener(adapterPosition)
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(ProjectRowBinding.inflate(LayoutInflater.from(parent.context), parent, false), mListener)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currenItem = projectList[position]
        holder.binding.tvProjectName.text = currenItem.projectName
        holder.binding.tvNumLocality.text = currenItem.numLocal.toString()
        holder.binding.tvNumMice.text = currenItem.numMice.toString()
        holder.binding.tvProjectDate.text = SimpleDateFormat.getDateTimeInstance().format(Date(currenItem.projectStart))
    }

    override fun getItemCount(): Int {
        return projectList.size
    }

    fun setData(projects: List<Project>){
        this.projectList = projects
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