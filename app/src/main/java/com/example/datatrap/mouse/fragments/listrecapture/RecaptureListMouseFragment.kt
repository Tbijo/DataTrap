package com.example.datatrap.mouse.fragments.listrecapture

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.datatrap.R
import com.example.datatrap.databinding.FragmentRecaptureListMouseBinding
import com.example.datatrap.models.Mouse
import com.example.datatrap.mouse.fragments.list.MouseRecyclerAdapter
import com.example.datatrap.viewmodels.MouseViewModel

class RecaptureListMouseFragment : Fragment(), SearchView.OnQueryTextListener {

    private var _binding: FragmentRecaptureListMouseBinding? = null
    private val binding get() = _binding!!
    private lateinit var mouseViewModel: MouseViewModel
    private lateinit var adapter: MouseRecyclerAdapter
    private var mouseList: List<Mouse> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        _binding = FragmentRecaptureListMouseBinding.inflate(inflater, container, false)
        mouseViewModel = ViewModelProvider(this).get(MouseViewModel::class.java)

        adapter = MouseRecyclerAdapter(this)
        val recyclerView = binding.recaptureRecyclerview
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter.setOnItemClickListener(object: MouseRecyclerAdapter.MyClickListener{
            override fun useClickListener(position: Int) {
                val mouse = mouseList[position]
                val action = RecaptureListMouseFragmentDirections.actionRecaptureListMouseFragmentToRecaptureMouseFragment2(mouse)
                findNavController().navigate(action)
            }

            override fun useLongClickListener(position: Int) {
                Toast.makeText(requireContext(), "You have found an easter egg. :D", Toast.LENGTH_SHORT).show()
            }

        })

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

    private fun searchMice(code: Int) {
        mouseViewModel.searchMice(code).observe(viewLifecycleOwner, Observer { mice ->
            mice.let {
                adapter.setData(it)
                mouseList = it
            }
        })
    }

}