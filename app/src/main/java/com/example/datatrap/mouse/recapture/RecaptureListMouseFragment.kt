package com.example.datatrap.mouse.fragments.recapture.list

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.datatrap.R
import com.example.datatrap.databinding.FragmentRecaptureListMouseBinding
import com.example.datatrap.mouse.data.SearchMouse
import com.example.datatrap.mouse.data.MouseRecapList
import com.example.datatrap.mouse.fragments.recapture.SearchRecaptureFragment
import com.example.datatrap.mouse.presentation.MouseViewModel
import com.example.datatrap.specie.presentation.SpecieViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class RecaptureListMouseFragment : Fragment(), SearchRecaptureFragment.DialogListener {

    private var _binding: FragmentRecaptureListMouseBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<RecaptureListMouseFragmentArgs>()
    private lateinit var adapter: RecaptureMouseRecyclerAdapter

    private val mouseViewModel: MouseViewModel by viewModels()
    private val specieViewModel: SpecieViewModel by viewModels()

    private var mouseList: List<MouseRecapList> = emptyList()
    private val specieMap: MutableMap<String, Long> = mutableMapOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        _binding = FragmentRecaptureListMouseBinding.inflate(inflater, container, false)

        adapter = RecaptureMouseRecyclerAdapter()
        val recyclerView = binding.recaptureRecyclerview
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val dividerItemDecoration = DividerItemDecoration(
            recyclerView.context,
            LinearLayoutManager.VERTICAL
        )
        recyclerView.addItemDecoration(dividerItemDecoration)

        adapter.setOnItemClickListener(object: RecaptureMouseRecyclerAdapter.MyClickListener {
            override fun useClickListener(position: Int) {
                goToRecaptureMouse(position)
            }
            override fun useLongClickListener(position: Int) {
                Toast.makeText(requireContext(), "You have found an easter egg. :D", Toast.LENGTH_SHORT).show()
            }
        })

        specieViewModel.getSpeciesForSelect().observe(viewLifecycleOwner) {
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

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.mouse_list_menu, menu)
        menu.findItem(R.id.menu_recapture).isVisible = false
        menu.findItem(R.id.menu_occ_info).isVisible = false
        menu.findItem(R.id.menu_search).isVisible = false
    }

    private fun goToRecaptureMouse(position: Int) {
        val mouse = mouseList[position]
        val action = RecaptureListMouseFragmentDirections.actionRecaptureListMouseFragmentToRecaptureMouseFragment(mouse, args.occList)
        findNavController().navigate(action)
    }

    override fun onDialogPositiveClick(searchMouse: SearchMouse) {
        val currenTime = Calendar.getInstance().time.time
        mouseViewModel.getMiceForRecapture(searchMouse.code, searchMouse.speciesID, searchMouse.sex,
            searchMouse.age, searchMouse.gravidity, searchMouse.sexActive, searchMouse.lactating,
            searchMouse.dateFrom, searchMouse.dateTo, currenTime).observe(viewLifecycleOwner) { mice ->
            mice.let {
                adapter.setData(it)
                mouseList = it
            }
        }
    }

    override fun onDialogNegativeClick() {
        Toast.makeText(requireContext(), "Search Canceled", Toast.LENGTH_LONG).show()
    }

}