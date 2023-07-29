package com.example.datatrap.mouse.list

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.datatrap.R
import com.example.datatrap.databinding.FragmentListOccMouseBinding
import com.example.datatrap.mouse.data.MouseOccList
import com.example.datatrap.mouse.presentation.MouseViewModel
import com.example.datatrap.core.data.pref.PrefViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListOccMouseFragment : Fragment() {

    private var _binding: FragmentListOccMouseBinding? = null
    private val binding get() = _binding!!

    private val mouseViewModel: MouseViewModel by viewModels()
    private val prefViewModel: PrefViewModel by viewModels()

    private lateinit var adapter: MouseRecyclerAdapter
    private val args by navArgs<ListOccMouseFragmentArgs>()

    private lateinit var mouseList: List<MouseOccList>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        _binding = FragmentListOccMouseBinding.inflate(inflater, container, false)

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

        adapter = MouseRecyclerAdapter()
        val recyclerView = binding.mouseRecyclerview
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val dividerItemDecoration = DividerItemDecoration(
            recyclerView.context,
            LinearLayoutManager.VERTICAL
        )
        recyclerView.addItemDecoration(dividerItemDecoration)

        mouseViewModel.getMiceForOccasion(args.occList.occasionId).observe(viewLifecycleOwner) { mice ->
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

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.mouse_list_menu, menu)
        menu.findItem(R.id.menu_search).isVisible = false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_recapture -> goToRecapture()
            R.id.menu_occ_info -> goToOccasionView()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun goToOccasionView() {
        val action = ListOccMouseFragmentDirections.actionListOccMouseFragmentToViewOccasionFragment(args.occList.occasionId)
        findNavController().navigate(action)
    }

    private fun goToRecapture() {
        val action = ListOccMouseFragmentDirections.actionListOccMouseFragmentToRecaptureListMouseFragment(args.occList)
        findNavController().navigate(action)
    }

}