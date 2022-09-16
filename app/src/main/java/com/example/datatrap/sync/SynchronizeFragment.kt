package com.example.datatrap.sync

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.example.datatrap.databinding.FragmentSynchronizeBinding
import com.example.datatrap.viewmodels.datastore.PrefViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SynchronizeFragment : Fragment() {

    private var _binding: FragmentSynchronizeBinding? = null
    private val binding get() = _binding!!

    private val prefViewModel: PrefViewModel by viewModels()
    private var lastSyncDate: Long? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        _binding = FragmentSynchronizeBinding.inflate(inflater, container, false)

        startSync("Loading Data")

        prefViewModel.readLastSyncDatePref.observe(viewLifecycleOwner) { date ->
            lastSyncDate = date
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun startSync(work: String) {
        // start
        binding.tvLoadMessage.text = work
        binding.tvLoadMessage.isVisible = true
        binding.loadingBar.isVisible = true
    }

    private fun endSync() {
        // end
        binding.tvLoadMessage.isVisible = false
        binding.loadingBar.isVisible = false
    }

}