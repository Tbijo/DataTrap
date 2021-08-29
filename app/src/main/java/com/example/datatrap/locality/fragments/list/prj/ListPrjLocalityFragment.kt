package com.example.datatrap.locality.fragments.list.prj

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.datatrap.databinding.FragmentListPrjLocalityBinding
import com.example.datatrap.models.Locality
import com.example.datatrap.models.relations.ProjectLocalityCrossRef
import com.example.datatrap.viewmodels.ProjectLocalityViewModel

class ListPrjLocalityFragment : Fragment() {

    private var _binding: FragmentListPrjLocalityBinding? = null
    private val binding get() = _binding!!
    private lateinit var prjLocalityViewModel: ProjectLocalityViewModel
    private lateinit var adapter: PrjLocalityRecyclerAdapter
    private val args by navArgs<ListPrjLocalityFragmentArgs>()
    private lateinit var localityList: List<Locality>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        _binding = FragmentListPrjLocalityBinding.inflate(inflater, container, false)
        prjLocalityViewModel = ViewModelProvider(this).get(ProjectLocalityViewModel::class.java)

        adapter = PrjLocalityRecyclerAdapter()
        val recyclerView = binding.prjLocalityRecyclerview
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        prjLocalityViewModel.getLocalitiesForProject(args.project.projectId).observe(viewLifecycleOwner, Observer {
            adapter.setData(it.first().localities)
            localityList = it.first().localities
        })

        adapter.setOnItemClickListener(object : PrjLocalityRecyclerAdapter.MyClickListener{
            // presun do sessionov s projektom a lokalitou
            override fun useClickListener(position: Int) {
                    val locality: Locality = localityList[position]
                    val action = ListPrjLocalityFragmentDirections.actionListPrjLocalityFragmentToListPrjSessionFragment(args.project, locality)
                    findNavController().navigate(action)
            }

            override fun useLongClickListener(position: Int) {
                // vymazat kombinaciu projektu a vybranej lokality
                    val locality: Locality = localityList[position]
                    val projectLocalityCrossRef = ProjectLocalityCrossRef(args.project.projectId, locality.localityId)
                    prjLocalityViewModel.deleteProjectLocality(projectLocalityCrossRef)
            }
        })

        binding.addLocalityFloatButton.setOnClickListener {
            // prechod do vsetkych lokalit na pracu s lokalitami a vytvorenie kombinacie project a locality
            val action = ListPrjLocalityFragmentDirections.actionListPrjLocalityFragmentToListAllLocalityFragment(args.project)
            findNavController().navigate(action)
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}