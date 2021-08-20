package com.example.datatrap.mainprj.fragments.list

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.datatrap.R
import com.example.datatrap.databinding.FragmentListProjectBinding
import com.example.datatrap.viewmodels.ProjectViewModel

class ListAllProjectFragment : Fragment(), SearchView.OnQueryTextListener {

    private var _binding: FragmentListProjectBinding? = null
    private val binding get() = _binding!!
    private lateinit var projectViewModel: ProjectViewModel
    private lateinit var adapter: ProjectRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        _binding = FragmentListProjectBinding.inflate(inflater, container, false)
        projectViewModel = ViewModelProvider(this).get(ProjectViewModel::class.java)

        adapter = ProjectRecyclerAdapter()
        binding.projectRecyclerview.adapter = adapter
        binding.projectRecyclerview.layoutManager =LinearLayoutManager(requireContext())

        projectViewModel.projectList.observe(viewLifecycleOwner, Observer { projects ->
            adapter.setData(projects)
        })

        binding.addProjectFloatButton.setOnClickListener {
            val action = ListAllProjectFragmentDirections.actionListAllProjectFragmentToAddProjectFragment()
            findNavController().navigate(action)
        }

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)
        val searchItem = menu.findItem(R.id.menu_search)
        val searchView = searchItem?.actionView as? SearchView
        searchView?.setOnQueryTextListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
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

        projectViewModel.searchProjects(searchQuery).observe(viewLifecycleOwner, Observer { projects ->
            projects.let {
                adapter.setData(it)
            }
        })
    }

}