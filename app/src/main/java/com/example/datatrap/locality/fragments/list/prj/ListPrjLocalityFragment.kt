package com.example.datatrap.locality.fragments.list.prj

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.datatrap.R
import com.example.datatrap.databinding.FragmentListPrjLocalityBinding
import com.example.datatrap.models.Locality
import com.example.datatrap.models.Project
import com.example.datatrap.models.relations.ProjectLocalityCrossRef
import com.example.datatrap.viewmodels.ProjectLocalityViewModel

class ListPrjLocalityFragment : Fragment() {

    private var _binding: FragmentListPrjLocalityBinding? = null
    private val binding get() = _binding!!
    private lateinit var prjLocalityViewModel: ProjectLocalityViewModel
    private lateinit var adapter: PrjLocalityRecyclerAdapter
    private val args by navArgs<ListPrjLocalityFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        _binding = FragmentListPrjLocalityBinding.inflate(inflater, container, false)
        prjLocalityViewModel = ViewModelProvider(this).get(ProjectLocalityViewModel::class.java)

        adapter = PrjLocalityRecyclerAdapter()
        val recyclerView = binding.prjLocalityRecyclerview
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        prjLocalityViewModel.getLocalitiesForProject(args.project.projectName).observe(viewLifecycleOwner, Observer {
            adapter.setData(it.first().localities)
        })

        adapter.setOnItemClickListener(object : PrjLocalityRecyclerAdapter.MyClickListener{
            override fun useClickListener(position: Int) {
                // presun do sessionov s projektom a lokalitou
                prjLocalityViewModel.getLocalitiesForProject(args.project.projectName).observe(viewLifecycleOwner, Observer {
                    val locality: Locality = it.first().localities[position]
                    val action = ListPrjLocalityFragmentDirections.actionListPrjLocalityFragmentToListPrjSessionFragment(args.project, locality)
                    findNavController().navigate(action)
                })
            }

            override fun useLongClickListener(position: Int) {
                // vymazat kombinaciu projektu a vybranej lokality
                prjLocalityViewModel.getLocalitiesForProject(args.project.projectName).observe(viewLifecycleOwner, Observer {
                    val locality: Locality = it.first().localities[position]
                    val projectLocalityCrossRef = ProjectLocalityCrossRef(args.project.projectName, locality.localityName)
                    prjLocalityViewModel.deleteProjectLocality(projectLocalityCrossRef)
                })
            }
        })

        binding.addLocalityFloatButton.setOnClickListener {

        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}