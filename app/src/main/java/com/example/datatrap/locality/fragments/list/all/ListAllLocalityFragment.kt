package com.example.datatrap.locality.fragments.list.all

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.datatrap.R
import com.example.datatrap.databinding.FragmentListAllLocalityBinding
import com.example.datatrap.locality.fragments.list.prj.PrjLocalityRecyclerAdapter
import com.example.datatrap.models.Locality
import com.example.datatrap.models.Project
import com.example.datatrap.models.relations.ProjectLocalityCrossRef
import com.example.datatrap.viewmodels.LocalityViewModel
import com.example.datatrap.viewmodels.ProjectLocalityViewModel

class ListAllLocalityFragment : Fragment(), SearchView.OnQueryTextListener {

    private var _binding: FragmentListAllLocalityBinding? = null
    private val binding get() = _binding!!
    private lateinit var localityViewModel: LocalityViewModel
    private lateinit var prjLocalityViewModel: ProjectLocalityViewModel
    private lateinit var adapter: PrjLocalityRecyclerAdapter
    private val args by navArgs<ListAllLocalityFragmentArgs>()
    private lateinit var localityList: List<Locality>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        _binding = FragmentListAllLocalityBinding.inflate(inflater, container, false)
        localityViewModel = ViewModelProvider(this).get(LocalityViewModel::class.java)
        prjLocalityViewModel = ViewModelProvider(this).get(ProjectLocalityViewModel::class.java)

        adapter = PrjLocalityRecyclerAdapter()
        val recyclerView = binding.localityRecyclerview
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        localityViewModel.localityList.observe(viewLifecycleOwner, Observer {localities ->
            adapter.setData(localities)
            localityList = localities
        })

        adapter.setOnItemClickListener(object : PrjLocalityRecyclerAdapter.MyClickListener{
            override fun useClickListener(position: Int) {
                // tu sa vytvori kombinacia project a locality a pojde sa spat do PrjLocality
                    val locality: Locality = localityList[position]
                    val project: Project = args.project
                    val projectLocalityCrossRef = ProjectLocalityCrossRef(project.projectId, locality.localityId)
                    prjLocalityViewModel.insertProjectLocality(projectLocalityCrossRef)
                    Toast.makeText(requireContext(), "Combination created.", Toast.LENGTH_SHORT).show()

                    findNavController().navigateUp()
            }

            override fun useLongClickListener(position: Int) {
                // tu sa pojde upravit alebo vymazat lokalita
                    val locality: Locality = localityList[position]
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

    private fun goToMap(){
        val action = ListAllLocalityFragmentDirections.actionListAllLocalityFragmentToLocalityMapFragment(
            localityList.toTypedArray()
        )
        findNavController().navigate(action)
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

    private fun searchLocalities(query: String?) {
        val searchQuery = "%$query%"
        localityViewModel.searchLocalities(searchQuery).observe(viewLifecycleOwner, Observer { localities ->
            localities.let {
                adapter.setData(it)
            }
        })
    }

}