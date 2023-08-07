package com.example.datatrap.session.presentation.session_prj_list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.datatrap.databinding.FragmentListPrjSessionBinding
import com.example.datatrap.session.data.Session
import com.example.datatrap.locality.data.LocalitySessionCrossRef
import com.example.datatrap.locality.LocalitySessionViewModel
import com.example.datatrap.core.data.pref.PrefViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class ListPrjSessionFragment : Fragment() {

    private var _binding: FragmentListPrjSessionBinding? = null
    private val binding get() = _binding!!

    private val sessionListViewModel: SessionListViewModel by viewModels()
    private val localitySessionViewModel: LocalitySessionViewModel by viewModels()
    private val prefViewModel: PrefViewModel by viewModels()

    private lateinit var adapter: PrjSessionRecyclerAdapter
    private val args by navArgs<ListPrjSessionFragmentArgs>()

    private lateinit var sessionList: List<Session>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListPrjSessionBinding.inflate(inflater, container, false)

        binding.tvSesPathPrjName.text = args.project.projectName
        binding.tvPathLocName.text = args.locList.localityName

        adapter = PrjSessionRecyclerAdapter()
        val recyclerView = binding.prjSessionRecyclerview
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val dividerItemDecoration = DividerItemDecoration(
            recyclerView.context,
            LinearLayoutManager.VERTICAL
        )
        recyclerView.addItemDecoration(dividerItemDecoration)

        sessionListViewModel.getSessionsForProject(args.project.projectId)
            .observe(viewLifecycleOwner) { sessions ->
                adapter.setData(sessions)
                sessionList = sessions
            }

        adapter.setOnItemClickListener(object : PrjSessionRecyclerAdapter.MyClickListener {
            override fun useClickListener(position: Int) {
                // vytvorit spojenie medzi vybranou locality a session
                val session: Session = sessionList[position]

                // nastavit vybranu session
                prefViewModel.saveSesNumPref(session.session)

                val kombLocSess =
                    LocalitySessionCrossRef(args.locList.localityId, session.sessionId)
                localitySessionViewModel.insertLocalitySessionCrossRef(kombLocSess)

                // ideme do occasion
                val action =
                    ListPrjSessionFragmentDirections.actionListPrjSessionFragmentToListSesOccasionFragment(
                        session, args.locList
                    )
                findNavController().navigate(action)
            }

            override fun useLongClickListener(position: Int) {
                // uprava vybranej session
                val session: Session = sessionList[position]
                val action =
                    ListPrjSessionFragmentDirections.actionListPrjSessionFragmentToUpdateSessionFragment(
                        session, args.locList
                    )
                findNavController().navigate(action)
            }
        })

        binding.addSessionFloatButton.setOnClickListener {
            // pridanie novej session
            showAddDialog("New Session", "Add new session?")
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun showAddDialog(title: String, message: String) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK") { _, _ ->
                insertSession()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }
            .create().show()
    }

    private fun insertSession() {
        val session: Session =
            Session(
                0,
                (sessionList.size + 1),
                args.project.projectId,
                0,
                Calendar.getInstance().time,
                null,
                Calendar.getInstance().time.time
            )

        // ulozit session
        sessionListViewModel.insertSession(session)

        Toast.makeText(requireContext(), "New session added.", Toast.LENGTH_SHORT).show()

    }


}