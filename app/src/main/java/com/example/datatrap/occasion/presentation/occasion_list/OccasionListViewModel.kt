package com.example.datatrap.occasion.presentation.occasion_list

import androidx.lifecycle.*
import com.example.datatrap.core.data.pref.PrefViewModel
import com.example.datatrap.occasion.data.Occasion
import com.example.datatrap.occasion.data.OccList
import com.example.datatrap.occasion.data.OccasionView
import com.example.datatrap.occasion.data.OccasionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class OccasionListViewModel @Inject constructor(
    private val occasionRepository: OccasionRepository
): ViewModel() {

    private val occasionListViewModel: OccasionListViewModel by viewModels()
    private val prefViewModel: PrefViewModel by viewModels()

    private var newOccasionNumber: Int = 0
    private lateinit var occList: List<OccList>

    val occasionId: MutableLiveData<Long> = MutableLiveData<Long>()

    init {
        holder.binding.tvOccDate.text = SimpleDateFormat.getDateTimeInstance().format(Date(currenItem.occasionStart))
        prefViewModel.readPrjNamePref.observe(viewLifecycleOwner) {
            binding.tvOccPathPrjName.text = it
        }
        binding.tvSesPathLocName.text = args.locList.localityName
        binding.tvSesNum.text = args.session.session.toString()

        occasionListViewModel.getOccasionsForSession(args.session.sessionId).observe(viewLifecycleOwner) {
            adapter.setData(it)
            occList = it
            newOccasionNumber = (it.size + 1)
        }

        adapter.setOnItemClickListener(object : OccasionRecyclerAdapter.MyClickListener {
            override fun useClickListener(position: Int) {
                // tu sa pojde do Mouse s occasion
                val action = ListSesOccasionFragmentDirections.actionListSesOccasionFragmentToListOccMouseFragment(occList[position])
                findNavController().navigate(action)
            }

            override fun useLongClickListener(position: Int) {
                // tu sa pojde do update occasion
                val action = ListSesOccasionFragmentDirections.actionListSesOccasionFragmentToUpdateOccasionFragment(occList[position], args.locList)
                findNavController().navigate(action)
            }

        })

        binding.addOccasionFloatButton.setOnClickListener {
            val action = ListSesOccasionFragmentDirections.actionListSesOccasionFragmentToAddOccasionFragment(args.session, args.locList, newOccasionNumber)
            findNavController().navigate(action)
        }
    }

    fun insertOccasion(occasion: Occasion) {
        viewModelScope.launch {
            occasionId.value = occasionRepository.insertOccasion(occasion)
        }
    }

    fun updateOccasion(occasion: Occasion) {
        viewModelScope.launch(Dispatchers.IO) {
            occasionRepository.updateOccasion(occasion)
        }
    }

    fun deleteOccasion(occasionId: Long, imagePath: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            val job1 = launch {
                if (imagePath != null) {
                    // odstranit fyzicku zlozku
                    val myFile = File(imagePath)
                    if (myFile.exists()) myFile.delete()
                }
            }
            val job2 = launch {
                occasionRepository.deleteOccasion(occasionId)
            }
            job1.join()
            job2.join()
        }
    }

    fun getOccasion(occasionId: Long): LiveData<Occasion> {
        return occasionRepository.getOccasion(occasionId)
    }

    fun getOccasionView(occasionId: Long): LiveData<OccasionView> {
        return occasionRepository.getOccasionView(occasionId)
    }

    fun getOccasionsForSession(idSession: Long): LiveData<List<OccList>> {
        return occasionRepository.getOccasionsForSession(idSession)
    }

}