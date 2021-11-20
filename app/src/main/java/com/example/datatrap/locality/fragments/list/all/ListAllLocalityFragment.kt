package com.example.datatrap.locality.fragments.list.all

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.datatrap.R
import com.example.datatrap.databinding.FragmentListAllLocalityBinding
import com.example.datatrap.locality.fragments.list.prj.PrjLocalityRecyclerAdapter
import com.example.datatrap.models.projectlocality.ProjectLocalityCrossRef
import com.example.datatrap.models.tuples.LocList
import com.example.datatrap.viewmodels.LocalityViewModel
import com.example.datatrap.viewmodels.ProjectLocalityViewModel
import com.example.datatrap.viewmodels.ProjectViewModel

class ListAllLocalityFragment : Fragment(), SearchView.OnQueryTextListener {

    private var _binding: FragmentListAllLocalityBinding? = null
    private val binding get() = _binding!!
    private lateinit var localityViewModel: LocalityViewModel
    private lateinit var prjLocalityViewModel: ProjectLocalityViewModel
    private lateinit var projectViewModel: ProjectViewModel
    private lateinit var adapter: PrjLocalityRecyclerAdapter
    private val args by navArgs<ListAllLocalityFragmentArgs>()
    private lateinit var localityList: List<LocList>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        _binding = FragmentListAllLocalityBinding.inflate(inflater, container, false)
        localityViewModel = ViewModelProvider(this).get(LocalityViewModel::class.java)
        prjLocalityViewModel = ViewModelProvider(this).get(ProjectLocalityViewModel::class.java)
        projectViewModel = ViewModelProvider(this).get(ProjectViewModel::class.java)

        adapter = PrjLocalityRecyclerAdapter()
        val recyclerView = binding.localityRecyclerview
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val dividerItemDecoration = DividerItemDecoration(
            recyclerView.context,
            LinearLayoutManager.VERTICAL
        )
        recyclerView.addItemDecoration(dividerItemDecoration)

        localityViewModel.localityList.observe(viewLifecycleOwner, { localities ->
            adapter.setData(localities)
            localityList = localities
        })

        adapter.setOnItemClickListener(object : PrjLocalityRecyclerAdapter.MyClickListener{
            override fun useClickListener(position: Int) {
                // tu sa vytvori kombinacia project a locality a pojde sa spat do PrjLocality
                insertCombination(position)

                Toast.makeText(requireContext(), "Association created.", Toast.LENGTH_SHORT).show()

                findNavController().navigateUp()
            }

            override fun useLongClickListener(position: Int) {
                // tu sa pojde upravit alebo vymazat lokalita
                val locality: LocList = localityList[position]
                val action = ListAllLocalityFragmentDirections.actionListAllLocalityFragmentToUpdateLocalityFragment(locality)
                findNavController().navigate(action)
            }

        })

        binding.addLocalityFloatButton.setOnClickListener {
            // tu sa pojde vytvorit nova lokalita
            val action = ListAllLocalityFragmentDirections.actionListAllLocalityFragmentToAddLocalityFragment()
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
        inflater.inflate(R.menu.list_locality_menu, menu)
        val searchItem = menu.findItem(R.id.menu_search)
        val searchView = searchItem?.actionView as? SearchView
        searchView?.setOnQueryTextListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_map -> goToMap()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText != null) {
            searchLocalities(newText)
        }
        return true
    }

    private fun insertCombination(position: Int) {
        val projectLocalityCrossRef = ProjectLocalityCrossRef(args.project.projectId, localityList[position].localityId)
        // vytvorit kombinaciu
        prjLocalityViewModel.insertProjectLocality(projectLocalityCrossRef)
    }

    private fun goToMap(){
        val action = ListAllLocalityFragmentDirections.actionListAllLocalityFragmentToLocalityMapFragment(
            localityList.toTypedArray()
        )
        findNavController().navigate(action)
    }

    private fun searchLocalities(query: String?) {
        val searchQuery = "%$query%"
        localityViewModel.searchLocalities(searchQuery).observe(viewLifecycleOwner, { localities ->
            localities.let {
                adapter.setData(it)
            }
        })
    }

}