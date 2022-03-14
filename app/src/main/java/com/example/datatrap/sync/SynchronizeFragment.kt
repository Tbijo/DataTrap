package com.example.datatrap.sync

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.example.datatrap.databinding.FragmentSynchronizeBinding
import com.example.datatrap.models.SynchronizeDate
import com.example.datatrap.viewmodels.SynchronizeDateViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class SynchronizeFragment : Fragment() {

    private var _binding: FragmentSynchronizeBinding? = null
    private val binding get() = _binding!!
    private val synchronizeDateViewModel: SynchronizeDateViewModel by viewModels()
    private var lastSyncDate: Date? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        _binding = FragmentSynchronizeBinding.inflate(inflater, container, false)

        synchronizeDateViewModel.getLastUpdateDate().observe(viewLifecycleOwner) {
            lastSyncDate = it.lastSyncDate
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun uploading() {
        // start
        binding.progressBar2.isVisible = true

        // last sync only rows newer than this
        lastSyncDate

        // update lastDate Sync
        val lastSyncDate = SynchronizeDate(1, Calendar.getInstance().time)
        synchronizeDateViewModel.updateLastSyncDate(lastSyncDate)

        // end
        binding.progressBar2.isVisible = false
    }

}