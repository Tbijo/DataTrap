package com.example.datatrap.settings.fragments.list.user.list

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.datatrap.databinding.UserRowBinding
import com.example.datatrap.models.User

class UserRecyclerView : RecyclerView.Adapter<UserRecyclerView.MyViewHolder>()  {

    private var userList = emptyList<User>()

    class MyViewHolder(val binding: UserRowBinding, listener: MyClickListener) : RecyclerView.ViewHolder(binding.root){
        init {
            binding.userRow.setOnClickListener {
                listener.useClickListener(adapterPosition)
            }

            binding.userRow.setOnLongClickListener {
                listener.useLongClickListener(adapterPosition)
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(UserRowBinding.inflate(LayoutInflater.from(parent.context), parent, false), mListener)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currenItem = userList[position]
        holder.binding.tvUserName.text = currenItem.userName
        holder.binding.tvActive.text = if (currenItem.isActive == 1) "Active" else "Inactive"
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    fun setData(users: List<User>){
        this.userList = users
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
