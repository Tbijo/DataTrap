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
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.datatrap.databinding.FragmentListPrjLocalityBinding
import com.example.datatrap.models.Locality
import com.example.datatrap.models.Project
import com.example.datatrap.models.relations.ProjectLocalityCrossRef
import com.example.datatrap.viewmodels.ProjectLocalityViewModel
import com.example.datatrap.viewmodels.ProjectViewModel
import java.util.*

class ListPrjLocalityFragment : Fragment() {

    private var _binding: FragmentListPrjLocalityBinding? = null
    private val binding get() = _binding!!
    private lateinit var prjLocalityViewModel: ProjectLocalityViewModel
    private lateinit var projectViewModel: ProjectViewModel
    private lateinit var adapter: PrjLocalityRecyclerAdapter
    private val args by navArgs<ListPrjLocalityFragmentArgs>()
    private lateinit var localityList: List<Locality>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        _binding = FragmentListPrjLocalityBinding.inflate(inflater, container, false)
        prjLocalityViewModel = ViewModelProvider(this).get(ProjectLocalityViewModel::class.java)
        projectViewModel = ViewModelProvider(this).get(ProjectViewModel::class.java)

        adapter = PrjLocalityRecyclerAdapter()
        val recyclerView = binding.prjLocalityRecyclerview
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val dividerItemDecoration = DividerItemDecoration(
            recyclerView.context,
            LinearLayoutManager.VERTICAL
        )
        recyclerView.addItemDecoration(dividerItemDecoration)

        prjLocalityViewModel.getLocalitiesForProject(args.project.projectId).observe(viewLifecycleOwner, Observer {
            adapter.setData(it.first().localities)
            localityList = it.first().localities
        })

        adapter.setOnItemClickListener(object : PrjLocalityRecyclerAdapter.MyClickListener{

            override fun useClickListener(position: Int) {
                // presun do sessionov s projektom a lokalitou
                goToSession(position)
            }

            override fun useLongClickListener(position: Int) {
                // znizit stlpec numLocal v projekte
                updateProjectNumLocal()

                // vymazat kombinaciu projektu a vybranej lokality
                deleteCombination(position)
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

    private fun goToSession(position: Int){
        val locality: Locality = localityList[position]
        val action = ListPrjLocalityFragmentDirections.actionListPrjLocalityFragmentToListPrjSessionFragment(args.project, locality)
        findNavController().navigate(action)
    }

    private fun deleteCombination(position: Int){
        val locality: Locality = localityList[position]
        val projectLocalityCrossRef = ProjectLocalityCrossRef(args.project.projectId, locality.localityId)
        prjLocalityViewModel.deleteProjectLocality(projectLocalityCrossRef)
    }

    private fun updateProjectNumLocal(){
        val updatedProject: Project = args.project
        updatedProject.numLocal = (updatedProject.numLocal - 1)
        updatedProject.projectDateTimeUpdated = Calendar.getInstance().time
        projectViewModel.updateProject(updatedProject)
    }

}