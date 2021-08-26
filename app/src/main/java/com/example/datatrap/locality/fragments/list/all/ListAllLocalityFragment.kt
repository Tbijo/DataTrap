package com.example.datatrap.locality.fragments.list.all

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.datatrap.databinding.FragmentListAllLocalityBinding
import com.example.datatrap.locality.fragments.list.prj.PrjLocalityRecyclerAdapter
import com.example.datatrap.models.Locality
import com.example.datatrap.models.Project
import com.example.datatrap.models.relations.ProjectLocalityCrossRef
import com.example.datatrap.viewmodels.LocalityViewModel
import com.example.datatrap.viewmodels.ProjectLocalityViewModel

class ListAllLocalityFragment : Fragment() {

    private var _binding: FragmentListAllLocalityBinding? = null
    private val binding get() = _binding!!
    private lateinit var localityViewModel: LocalityViewModel
    private lateinit var prjLocalityViewModel: ProjectLocalityViewModel
    private lateinit var adapter: PrjLocalityRecyclerAdapter
    private val args by navArgs<ListAllLocalityFragmentArgs>()

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
        })

        adapter.setOnItemClickListener(object : PrjLocalityRecyclerAdapter.MyClickListener{
            override fun useClickListener(position: Int) {
                // tu sa vytvori kombinacia project a locality a pojde sa spat do PrjLocality
                localityViewModel.localityList.observe(viewLifecycleOwner, Observer { localities ->
                    val locality: Locality = localities[position]
                    val project: Project = args.project
                    val projectLocalityCrossRef = ProjectLocalityCrossRef(project.projectName, locality.localityName)
                    prjLocalityViewModel.insertProjectLocality(projectLocalityCrossRef)
                    Toast.makeText(requireContext(), "Combination created.", Toast.LENGTH_SHORT).show()
                    val action = ListAllLocalityFragmentDirections.actionListAllLocalityFragmentToListPrjLocalityFragment(args.project)
                    findNavController().navigate(action)
                })
            }

            override fun useLongClickListener(position: Int) {
                // tu sa pojde upravit alebo vymazat lokalita
                localityViewModel.localityList.observe(viewLifecycleOwner, Observer { localities ->
                    val locality: Locality = localities[position]
                    val action = ListAllLocalityFragmentDirections.actionListAllLocalityFragmentToUpdateLocalityFragment(locality, args.project)
                    findNavController().navigate(action)
                })
            }

        })

        binding.addLocalityFloatButton.setOnClickListener {
            // tu sa pojde vytvorit nova lokalita
            val action = ListAllLocalityFragmentDirections.actionListAllLocalityFragmentToAddLocalityFragment(args.project)
            findNavController().navigate(action)
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}