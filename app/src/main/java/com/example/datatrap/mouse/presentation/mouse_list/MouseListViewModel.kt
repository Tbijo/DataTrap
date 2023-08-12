package com.example.datatrap.mouse.presentation.mouse_list

import androidx.lifecycle.*
import com.example.datatrap.R
import com.example.datatrap.core.data.pref.PrefViewModel
import com.example.datatrap.mouse.data.Mouse
import com.example.datatrap.mouse.domain.model.MouseLog
import com.example.datatrap.mouse.domain.model.MouseOccList
import com.example.datatrap.mouse.domain.model.MouseRecapList
import com.example.datatrap.mouse.domain.model.MouseView
import com.example.datatrap.mouse.data.MouseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import javax.inject.Inject

@HiltViewModel
class MouseListViewModel @Inject constructor(
    private val mouseRepository: MouseRepository
): ViewModel() {

    val mouseId: MutableLiveData<Long> = MutableLiveData<Long>()
    private var mouseList = emptyList<MouseOccList>()
    private val mouseListViewModel: MouseListViewModel by viewModels()
    private val prefViewModel: PrefViewModel by viewModels()

    init {
        val currenItem = mouseList[position]

        holder.binding.tvIdIndividual.text = currenItem.mouseCode.toString()

        holder.binding.tvMouseSpecieCode.text = currenItem.specieCode

        val dateFormated = SimpleDateFormat.getDateTimeInstance().format(Date(currenItem.mouseCaught))
        holder.binding.tvCatchDateTime.text = dateFormated

        // fragment
        prefViewModel.readPrjNamePref.observe(viewLifecycleOwner) {
            binding.tvMoPathPrjName.text = it
        }
        prefViewModel.readLocNamePref.observe(viewLifecycleOwner) {
            binding.tvMoPathLocName.text = it
        }
        prefViewModel.readSesNumPref.observe(viewLifecycleOwner) {
            binding.tvMoPathSesNum.text = it.toString()
        }
        binding.tvPathOccNum.text = args.occList.occasion.toString()

        mouseListViewModel.getMiceForOccasion(args.occList.occasionId).observe(viewLifecycleOwner) { mice ->
            adapter.setData(mice)
            mouseList = mice
        }

        adapter.setOnItemClickListener(object : MouseRecyclerAdapter.MyClickListener {
            override fun useClickListener(position: Int) {
                //view
                val action = ListOccMouseFragmentDirections.actionListOccMouseFragmentToViewMouseFragment(mouseList[position])
                findNavController().navigate(action)
            }

            override fun useLongClickListener(position: Int) {
                //update
                val action = ListOccMouseFragmentDirections.actionListOccMouseFragmentToUpdateMouseFragment(args.occList, mouseList[position])
                findNavController().navigate(action)
            }

        })

        binding.addMouseFloatButton.setOnClickListener {
            // pridat novu mys
            val action = ListOccMouseFragmentDirections.actionListOccMouseFragmentToAddNewMouseFragment(args.occList)
            findNavController().navigate(action)
        }

        binding.addMouseFloatButton.setOnLongClickListener {
            val action = ListOccMouseFragmentDirections.actionListOccMouseFragmentToAddMultiMouseFragment(args.occList)
            findNavController().navigate(action)
            true
        }
        R.id.menu_recapture -> goToRecapture()
        R.id.menu_occ_info -> goToOccasionView()
    }

    private fun goToOccasionView() {
        val action = ListOccMouseFragmentDirections.actionListOccMouseFragmentToViewOccasionFragment(args.occList.occasionId)
        findNavController().navigate(action)
    }

    private fun goToRecapture() {
        val action = ListOccMouseFragmentDirections.actionListOccMouseFragmentToRecaptureListMouseFragment(args.occList)
        findNavController().navigate(action)
    }

    fun insertMouse(mouse: Mouse) {
        viewModelScope.launch {
            mouseId.value = mouseRepository.insertMouse(mouse)
        }
    }

    fun updateMouse(mouse: Mouse) {
        viewModelScope.launch(Dispatchers.IO) {
            mouseRepository.updateMouse(mouse)
        }
    }

    fun deleteMouse(mouseId: Long, imagePath: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            val job1 = launch {
                if (imagePath != null) {
                    // odstranit fyzicku zlozku
                    val myFile = File(imagePath)
                    if (myFile.exists()) myFile.delete()
                }
            }
            val job2 = launch {
                mouseRepository.deleteMouse(mouseId)
            }
            job1.join()
            job2.join()
        }
    }

    fun getMouse(mouseId: Long): LiveData<Mouse> {
        return mouseRepository.getMouse(mouseId)
    }

    fun getMouseForView(idMouse: Long): LiveData<MouseView> {
        return mouseRepository.getMouseForView(idMouse)
    }

    fun getMiceForOccasion(idOccasion: Long): LiveData<List<MouseOccList>> {
        return mouseRepository.getMiceForOccasion(idOccasion)
    }

    fun getMiceForRecapture(code: Int?, specieID: Long?, sex: String?, age: String?, gravidity: Boolean, sexActive: Boolean, lactating: Boolean, dateFrom: Long?, dateTo: Long?, currentTime: Long): LiveData<List<MouseRecapList>> {
        return mouseRepository.getMiceForRecapture(code, specieID, sex, age, gravidity, sexActive, lactating, dateFrom, dateTo, currentTime)
    }

    fun getMiceForLog(primeMouseID: Long, deviceID: String): LiveData<List<MouseLog>> {
        return mouseRepository.getMiceForLog(primeMouseID, deviceID)
    }

    fun getActiveCodeOfLocality(localityId: Long, currentTime: Long): LiveData<List<Int>> {
        return mouseRepository.getActiveCodeOfLocality(localityId, currentTime)
    }

    fun countMiceForLocality(localityId: Long): LiveData<Int> {
        return mouseRepository.countMiceForLocality(localityId)
    }

    fun insertMultiMouse(mice: List<Mouse>) {
        viewModelScope.launch(Dispatchers.IO) {
            mouseRepository.insertMultiMouse(mice)
        }
    }

    fun getTrapsIdInOccasion(occasionId: Long): LiveData<List<Int>> {
        return mouseRepository.getTrapsIdInOccasion(occasionId)
    }
}