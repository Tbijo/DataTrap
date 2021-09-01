package com.example.datatrap.mouse.fragments.list

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.datatrap.R
import com.example.datatrap.databinding.FragmentListOccMouseBinding
import com.example.datatrap.models.Mouse
import com.example.datatrap.viewmodels.MouseViewModel
import com.example.datatrap.viewmodels.SpecieViewModel

class ListOccMouseFragment : Fragment(), SearchView.OnQueryTextListener {

    private var _binding: FragmentListOccMouseBinding? = null
    private val binding get() = _binding!!
    private lateinit var mouseViewModel: MouseViewModel
    private lateinit var specieViewModel: SpecieViewModel
    private lateinit var adapter: MouseRecyclerAdapter
    private val args by navArgs<ListOccMouseFragmentArgs>()
    private lateinit var mouseList: List<Mouse>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        _binding = FragmentListOccMouseBinding.inflate(inflater, container, false)
        mouseViewModel = ViewModelProvider(this).get(MouseViewModel::class.java)
        specieViewModel = ViewModelProvider(this).get(SpecieViewModel::class.java)

        adapter = MouseRecyclerAdapter()
        val recyclerView = binding.mouseRecyclerview
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        mouseViewModel.getMiceForOccasion(args.occasion.occasionId).observe(viewLifecycleOwner, Observer { mice ->
            adapter.setData(mice, specieViewModel.specieList.value!!)
            mouseList = mice
        })

        adapter.setOnItemClickListener(object : MouseRecyclerAdapter.MyClickListener{
            override fun useClickListener(position: Int) {
                //view
                val action = ListOccMouseFragmentDirections.actionListOccMouseFragmentToViewMouseFragment(mouseList[position])
                findNavController().navigate(action)
            }

            override fun useLongClickListener(position: Int) {
                //update
                val action = ListOccMouseFragmentDirections.actionListOccMouseFragmentToUpdateMouseFragment(mouseList[position], args.occasion)
                findNavController().navigate(action)
            }

        })

        binding.addMouseFloatButton.setOnClickListener {
            // pridat novu mys
            val action = ListOccMouseFragmentDirections.actionListOccMouseFragmentToAddNewMouseFragment(args.occasion)
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
            searchMice(Integer.parseInt(newText))
        }
        return true
    }

    private fun searchMice(query: Int) {
        mouseViewModel.searchMice(query).observe(viewLifecycleOwner, Observer { localities ->
            localities.let {
                adapter.setData(it, specieViewModel.specieList.value!!)
            }
        })
    }

}