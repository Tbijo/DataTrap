package com.example.datatrap.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.datatrap.databinding.FragmentListMainBinding

class ListMainFragment : Fragment() {

    private var _binding: FragmentListMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: SettingsRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        _binding = FragmentListMainBinding.inflate(inflater, container, false)

        adapter = SettingsRecyclerAdapter()
        binding.settingsRecyclerview.adapter = adapter
        binding.settingsRecyclerview.layoutManager = LinearLayoutManager(requireContext())

        val dividerItemDecoration = DividerItemDecoration(
            binding.settingsRecyclerview.context,
            LinearLayoutManager.VERTICAL
        )
        binding.settingsRecyclerview.addItemDecoration(dividerItemDecoration)

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}