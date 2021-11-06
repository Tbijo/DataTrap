package com.example.datatrap.mouse.fragments.recapture.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.datatrap.databinding.RecaptureRowBinding
import com.example.datatrap.models.Locality
import com.example.datatrap.models.Mouse
import com.example.datatrap.models.Specie
import com.example.datatrap.viewmodels.LocalityViewModel
import com.example.datatrap.viewmodels.SpecieViewModel

class RecaptureMouseRecyclerAdapter(owner: ViewModelStoreOwner, private val viewLifecycleOwner: LifecycleOwner) : RecyclerView.Adapter<RecaptureMouseRecyclerAdapter.MyViewHolder>() {

    private var mouseList = emptyList<Mouse>()
    private val specieViewModel: SpecieViewModel = ViewModelProvider(owner).get(SpecieViewModel::class.java)
    private val localityViewModel: LocalityViewModel = ViewModelProvider(owner).get(LocalityViewModel::class.java)
    private lateinit var specieList: List<Specie>
    private lateinit var localList: List<Locality>

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

        holder.binding.tvMouseId.text = currenItem.code.toString()

        holder.binding.tvAge.text = currenItem.age.toString()

        holder.binding.tvWeight.text = currenItem.weight.toString()

        holder.binding.tvSex.text = currenItem.sex.toString()

        holder.binding.tvGravitRecap.text = if (currenItem.gravidity == true) "Yes" else "No"

        holder.binding.tvLactaRecap.text = if (currenItem.lactating == true) "Yes" else "No"

        holder.binding.tvSexActiveRecap.text = if (currenItem.sexActive == true) "Yes" else "No"

        localityViewModel.localityList.observe(viewLifecycleOwner, { localList ->
            holder.binding.tvLocality.text = localList.first {it.localityId == currenItem.localityID}.localityName
        })

        specieViewModel.specieList.observe(viewLifecycleOwner, { specieList ->
            holder.binding.tvSpecieRecap.text = specieList.first { it.specieId == currenItem.speciesID }.speciesCode
        })

        holder.binding.tvRecapDate.text = "${currenItem.mouseDateTimeCreated}"
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
