package com.example.datatrap.mouse.fragments.multi

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.datatrap.R
import com.example.datatrap.databinding.MouseMultiRowBinding
import com.example.datatrap.models.other.MultiMouse
import com.example.datatrap.myenums.EnumSpecie

class MouseMultiRecyclerAdapter(trapIdList: List<Int>, val context: Context) : RecyclerView.Adapter<MouseMultiRecyclerAdapter.MyViewHolder>()  {

    private var specieList = EnumSpecie.values().map {
        it.name
    }

    private var size: Int = 1

    private var mapData: MutableMap<Int, MultiMouse> = mutableMapOf()

    private val dropDownArrTrapID = ArrayAdapter(context, R.layout.dropdown_names, trapIdList)
    private val dropDownArrSpecie = ArrayAdapter(context, R.layout.dropdown_names, specieList)

    // referencia na jeden riadok
    class MyViewHolder(val binding: MouseMultiRowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(MouseMultiRowBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.binding.autoCompTvTrapId.setAdapter(dropDownArrTrapID)
        holder.binding.autoCompTvTrapId.setText("")

        holder.binding.autoCompTvSpecie.setAdapter(dropDownArrSpecie)
        holder.binding.autoCompTvSpecie.setText("")

        holder.binding.autoCompTvTrapId.setOnItemClickListener{ parent, _, itemPosition, _ ->
            val name: String = parent.getItemAtPosition(itemPosition).toString()
            if (mapData[position] == null) {
                val multiMouse = MultiMouse(Integer.parseInt(name), null)
                mapData[position] = multiMouse
            } else {
                mapData[position]?.trapID = Integer.parseInt(name)
            }
        }

        holder.binding.autoCompTvSpecie.setOnItemClickListener{ parent, _, itemPosition, _ ->
            val name: String = parent.getItemAtPosition(itemPosition) as String
            if (mapData[position] == null) {
                val multiMouse = MultiMouse(null, name)
                mapData[position] = multiMouse
            } else {
                mapData[position]?.specieID = name
            }
        }
    }

    override fun getItemCount(): Int {
        return size
    }

    fun addRow(){
        this.size += 1
        notifyItemInserted(size)
    }

    fun removeRow(){
        if (this.size - 1 >= 0) {
            this.size -= 1
            notifyItemRemoved(size)
            mapData.remove(size)
        }
    }

    fun getData() = mapData

}