package com.example.datatrap.session.fragments.list

import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.datatrap.databinding.FragmentListPrjSessionBinding
import com.example.datatrap.models.Locality
import com.example.datatrap.models.Session
import com.example.datatrap.viewmodels.LocalityViewModel
import com.example.datatrap.viewmodels.SessionViewModel
import java.util.*

class ListPrjSessionFragment : Fragment() {

    private var _binding: FragmentListPrjSessionBinding? = null
    private val binding get() = _binding!!
    private lateinit var sessionViewModel: SessionViewModel
    private lateinit var localityViewModel: LocalityViewModel
    private lateinit var adapter: PrjSessionRecyclerAdapter
    private val args by navArgs<ListPrjSessionFragmentArgs>()
    private lateinit var sessionList: List<Session>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        _binding = FragmentListPrjSessionBinding.inflate(inflater, container, false)
        sessionViewModel = ViewModelProvider(this).get(SessionViewModel::class.java)
        localityViewModel = ViewModelProvider(this).get(LocalityViewModel::class.java)

        adapter = PrjSessionRecyclerAdapter()
        val recyclerView = binding.prjSessionRecyclerview
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        sessionViewModel.getSessionsForProject(args.project.projectId)
            .observe(viewLifecycleOwner, Observer { sessions ->
                adapter.setData(sessions)
                sessionList = sessions
            })

        adapter.setOnItemClickListener(object : PrjSessionRecyclerAdapter.MyClickListener {
            override fun useClickListener(position: Int) {
                // ideme do occasion
                val session: Session = sessionList[position]
                val action =
                    ListPrjSessionFragmentDirections.actionListPrjSessionFragmentToListSesOccasionFragment(
                        session,
                        args.locality
                    )
                findNavController().navigate(action)
            }

            override fun useLongClickListener(position: Int) {
                // uprava vybranej session
                val session: Session = sessionList[position]
                val action =
                    ListPrjSessionFragmentDirections.actionListPrjSessionFragmentToUpdateSessionFragment(
                        session, args.locality
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
        val deviceID: String = Settings.Secure.getString(requireContext().contentResolver, Settings.Secure.ANDROID_ID)
        val session: Session =
            Session(0, (sessionList.size + 1), deviceID, args.project.projectId, 0, Calendar.getInstance().time, null)

        // zvacsit numSes v lokalite
        updateLocalityNumSess()

        // ulozit session
        sessionViewModel.insertSession(session)

        Toast.makeText(requireContext(), "New session added.", Toast.LENGTH_SHORT).show()

    }

    private fun updateLocalityNumSess(){
        val updatedLocality: Locality = localityViewModel.getLocality(args.locality.localityId).value!!
        updatedLocality.numSessions = (updatedLocality.numSessions + 1)
        localityViewModel.updateLocality(updatedLocality)
    }

}