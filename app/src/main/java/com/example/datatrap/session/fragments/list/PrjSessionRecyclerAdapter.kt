package com.example.datatrap.session.fragments.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.datatrap.databinding.SessionRowBinding
import com.example.datatrap.models.Session
import java.text.SimpleDateFormat
import java.util.*

class PrjSessionRecyclerAdapter : RecyclerView.Adapter<PrjSessionRecyclerAdapter.MyViewHolder>() {

    private var sessionList = emptyList<Session>()

    class MyViewHolder(val binding: SessionRowBinding, listener: MyClickListener) : RecyclerView.ViewHolder(binding.root){
        init {
            binding.sessionRow.setOnClickListener {
                listener.useClickListener(adapterPosition)
            }

            binding.sessionRow.setOnLongClickListener {
                listener.useLongClickListener(adapterPosition)
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(SessionRowBinding.inflate(LayoutInflater.from(parent.context), parent, false), mListener)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currenItem = sessionList[position]
        holder.binding.tvSession.text = currenItem.session.toString()
        holder.binding.tvSessionDate.text = SimpleDateFormat.getDateTimeInstance().format(Date(currenItem.sessionStart))
        holder.binding.tvNumOcc.text = currenItem.numOcc.toString()
    }

    override fun getItemCount(): Int {
        return sessionList.size
    }

    fun setData(sessions: List<Session>){
        this.sessionList = sessions
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
