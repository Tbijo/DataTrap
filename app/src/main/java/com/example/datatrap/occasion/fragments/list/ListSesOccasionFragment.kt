package com.example.datatrap.occasion.fragments.list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.datatrap.databinding.FragmentListSesOccasionBinding
import com.example.datatrap.models.tuples.OccList
import com.example.datatrap.viewmodels.OccasionViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListSesOccasionFragment : Fragment() {

    private var _binding: FragmentListSesOccasionBinding? = null
    private val binding get() = _binding!!
    private val occasionViewModel: OccasionViewModel by viewModels()
    private lateinit var adapter: OccasionRecyclerAdapter
    private val args by navArgs<ListSesOccasionFragmentArgs>()

    private var newOccasionNumber: Int = 0
    private lateinit var occList: List<OccList>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        _binding = FragmentListSesOccasionBinding.inflate(inflater, container, false)

        adapter = OccasionRecyclerAdapter()
        val recyclerView = binding.sesOccasionRecyclerview
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val dividerItemDecoration = DividerItemDecoration(
            recyclerView.context,
            LinearLayoutManager.VERTICAL
        )
        recyclerView.addItemDecoration(dividerItemDecoration)

        occasionViewModel.getOccasionsForSession(args.session.sessionId).observe(viewLifecycleOwner) {
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

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}