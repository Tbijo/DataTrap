package com.example.datatrap.occasion.fragments.list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.datatrap.databinding.FragmentListSesOccasionBinding
import com.example.datatrap.models.Occasion
import com.example.datatrap.viewmodels.OccasionViewModel

class ListSesOccasionFragment : Fragment() {

    private var _binding: FragmentListSesOccasionBinding? = null
    private val binding get() = _binding!!
    private lateinit var occasionViewModel: OccasionViewModel
    private lateinit var adapter: OccasionRecyclerAdapter
    private val args by navArgs<ListSesOccasionFragmentArgs>()
    private lateinit var occasionList: List<Occasion>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        _binding = FragmentListSesOccasionBinding.inflate(inflater, container, false)
        occasionViewModel = ViewModelProvider(this).get(OccasionViewModel::class.java)

        adapter = OccasionRecyclerAdapter()
        val recyclerView = binding.sesOccasionRecyclerview
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        return binding.root
    }

}