package com.example.datatrap.specie.fragments.list

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.datatrap.R
import com.example.datatrap.databinding.FragmentListSpecieBinding
import com.example.datatrap.viewmodels.SpecieViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListSpecieFragment : Fragment(), SearchView.OnQueryTextListener {

    private var _binding: FragmentListSpecieBinding? = null
    private val binding get() = _binding!!
    private val specieViewModel: SpecieViewModel by viewModels()
    private lateinit var adapter: SpecieRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        _binding = FragmentListSpecieBinding.inflate(inflater, container, false)

        adapter = SpecieRecyclerAdapter()
        binding.specieRecyclerview.adapter = adapter
        binding.specieRecyclerview.layoutManager = LinearLayoutManager(requireContext())

        val dividerItemDecoration = DividerItemDecoration(
            binding.specieRecyclerview.context,
            LinearLayoutManager.VERTICAL
        )
        binding.specieRecyclerview.addItemDecoration(dividerItemDecoration)

        specieViewModel.specieList.observe(viewLifecycleOwner) { species ->
            adapter.setData(species)
        }

        binding.addSpecieFloatButton.setOnClickListener{
            val action = ListSpecieFragmentDirections.actionListSpecieFragmentToAddSpecieFragment()
            findNavController().navigate(action)
        }

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)
        val searchItem = menu.findItem(R.id.menu_search)
        val searchView = searchItem?.actionView as? SearchView
        searchView?.setOnQueryTextListener(this)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText != null) {
            searchForData(newText)
        }
        return true
    }

    private fun searchForData(query: String?) {
        val searchQuery = "%$query%"
        specieViewModel.searchSpecies(searchQuery).observe(viewLifecycleOwner) { species ->
            species.let {
                adapter.setData(it)
            }
        }
    }

}