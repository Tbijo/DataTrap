package com.example.datatrap.mouse.presentation.mouse_recapture_list

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.datatrap.mouse.domain.model.MouseRecapList
import com.example.datatrap.mouse.domain.model.SearchMouse
import com.example.datatrap.mouse.presentation.mouse_list.MouseListViewModel
import com.example.datatrap.specie.presentation.specie_list.SpecieListViewModel
import java.text.SimpleDateFormat
import java.util.Calendar

class RecaptureListViewModel: ViewModel() {

    private val mouseListViewModel: MouseListViewModel by viewModels()
    private val specieListViewModel: SpecieListViewModel by viewModels()

    private var mouseList: List<MouseRecapList> = emptyList()
    private val specieMap: MutableMap<String, Long> = mutableMapOf()

    init {
        val currenItem = mouseList[position]

        holder.binding.tvMouseCode.text = currenItem.code.toString()

        holder.binding.tvAge.text = currenItem.age.toString()

        holder.binding.tvWeight.text = currenItem.weight.toString()

        holder.binding.tvSex.text = currenItem.sex.toString()

        holder.binding.tvGravitRecap.text = when (currenItem.gravidity) {
            true -> "Yes"
            false -> "No"
            else -> "null"
        }

        holder.binding.tvLactaRecap.text = when (currenItem.lactating) {
            true -> "Yes"
            false -> "No"
            else -> "null"
        }

        holder.binding.tvSexActiveRecap.text = if (currenItem.sexActive) "Yes" else "No"

        holder.binding.tvLocality.text = currenItem.localityName

        holder.binding.tvSpecieRecap.text = currenItem.specieCode

        val dateFormated = SimpleDateFormat.getDateTimeInstance().format(Date(currenItem.mouseCaught))
        holder.binding.tvRecapDate.text = dateFormated

        adapter.setOnItemClickListener(object: RecaptureMouseRecyclerAdapter.MyClickListener {
            override fun useClickListener(position: Int) {
                goToRecaptureMouse(position)
            }
            override fun useLongClickListener(position: Int) {
                Toast.makeText(requireContext(), "You have found an easter egg. :D", Toast.LENGTH_SHORT).show()
            }
        })

        specieListViewModel.getSpeciesForSelect().observe(viewLifecycleOwner) {
            it.forEach { specie ->
                specieMap[specie.speciesCode] = specie.specieId
            }
        }

        binding.searchMouseFloatBtn.setOnClickListener {
            // zobrazit formular na vyhliadanie
            val searchDialogFragment = SearchRecaptureFragment(specieMap)
            //parentFragmentManager
            searchDialogFragment.show(childFragmentManager, "search")
        }
    }

    fun getMiceForRecapture(code: Int?, specieID: Long?, sex: String?, age: String?, gravidity: Boolean, sexActive: Boolean, lactating: Boolean, dateFrom: Long?, dateTo: Long?, currentTime: Long): LiveData<List<MouseRecapList>> {
        return mouseRepository.getMiceForRecapture(code, specieID, sex, age, gravidity, sexActive, lactating, dateFrom, dateTo, currentTime)
    }

    private fun goToRecaptureMouse(position: Int) {
        val mouse = mouseList[position]
        val action = RecaptureListMouseFragmentDirections.actionRecaptureListMouseFragmentToRecaptureMouseFragment(mouse, args.occList)
        findNavController().navigate(action)
    }

    fun onDialogPositiveClick(searchMouse: SearchMouse) {
        val currenTime = Calendar.getInstance().time.time
        mouseListViewModel.getMiceForRecapture(searchMouse.code, searchMouse.speciesID, searchMouse.sex,
            searchMouse.age, searchMouse.gravidity, searchMouse.sexActive, searchMouse.lactating,
            searchMouse.dateFrom, searchMouse.dateTo, currenTime).observe(viewLifecycleOwner) { mice ->
            mice.let {
                adapter.setData(it)
                mouseList = it
            }
        }
    }

    fun onDialogNegativeClick() {
        Toast.makeText(requireContext(), "Search Canceled", Toast.LENGTH_LONG).show()
    }
}