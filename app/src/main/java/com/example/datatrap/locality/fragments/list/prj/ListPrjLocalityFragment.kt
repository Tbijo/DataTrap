package com.example.datatrap.locality.fragments.list.prj

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.datatrap.databinding.FragmentListPrjLocalityBinding
import com.example.datatrap.models.projectlocality.ProjectLocalityCrossRef
import com.example.datatrap.models.tuples.LocList
import com.example.datatrap.viewmodels.ProjectLocalityViewModel
import com.example.datatrap.viewmodels.datastore.PrefViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListPrjLocalityFragment : Fragment() {

    private var _binding: FragmentListPrjLocalityBinding? = null
    private val binding get() = _binding!!

    private val prjLocalityViewModel: ProjectLocalityViewModel by viewModels()
    private val prefViewModel: PrefViewModel by viewModels()

    private lateinit var adapter: PrjLocalityRecyclerAdapter
    private val args by navArgs<ListPrjLocalityFragmentArgs>()
    private lateinit var localityList: List<LocList>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentListPrjLocalityBinding.inflate(inflater, container, false)

        binding.tvPathPrjName.text = args.project.projectName

        adapter = PrjLocalityRecyclerAdapter()
        val recyclerView = binding.prjLocalityRecyclerview
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val dividerItemDecoration = DividerItemDecoration(
            recyclerView.context,
            LinearLayoutManager.VERTICAL
        )
        recyclerView.addItemDecoration(dividerItemDecoration)

        prjLocalityViewModel.getLocalitiesForProject(args.project.projectId)
            .observe(viewLifecycleOwner) {
                val locList = mutableListOf<LocList>()
                it.first().localities.forEach { locality ->
                    val loc = LocList(
                        locality.localityId,
                        locality.localityName,
                        locality.localityDateTimeCreated,
                        locality.xA,
                        locality.yA,
                        locality.numSessions
                    )
                    locList.add(loc)
                }
                adapter.setData(locList)
                localityList = locList
            }

        adapter.setOnItemClickListener(object : PrjLocalityRecyclerAdapter.MyClickListener {

            override fun useClickListener(position: Int) {
                // nastavit vybranu lokalitu
                prefViewModel.saveLocNamePref(localityList[position].localityName)
                // presun do sessionov s projektom a lokalitou
                goToSession(position)
            }

            override fun useLongClickListener(position: Int) {
                val builder = AlertDialog.Builder(requireContext())
                builder.setPositiveButton("Yes") { _, _ ->

                    // vymazat kombinaciu projektu a vybranej lokality
                    deleteCombination(position)

                    Toast.makeText(requireContext(), "Association deleted.", Toast.LENGTH_LONG)
                        .show()
                }
                    .setNegativeButton("No") { _, _ -> }
                    .setTitle("Delete Association?")
                    .setMessage("Are you sure you want to delete this association?")
                    .create().show()
            }
        })

        binding.addLocalityFloatButton.setOnClickListener {
            // prechod do vsetkych lokalit na pracu s lokalitami a vytvorenie kombinacie project a locality
            val action =
                ListPrjLocalityFragmentDirections.actionListPrjLocalityFragmentToListAllLocalityFragment(
                    args.project
                )
            findNavController().navigate(action)
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun goToSession(position: Int) {
        val action =
            ListPrjLocalityFragmentDirections.actionListPrjLocalityFragmentToListPrjSessionFragment(
                args.project,
                localityList[position]
            )
        findNavController().navigate(action)
    }

    private fun deleteCombination(position: Int) {
        val projectLocalityCrossRef =
            ProjectLocalityCrossRef(args.project.projectId, localityList[position].localityId)
        prjLocalityViewModel.deleteProjectLocality(projectLocalityCrossRef)
    }

}