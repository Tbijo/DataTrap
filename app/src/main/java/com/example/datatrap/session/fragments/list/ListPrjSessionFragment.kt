package com.example.datatrap.session.fragments.list

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
import com.example.datatrap.databinding.FragmentListPrjSessionBinding
import com.example.datatrap.models.Session
import com.example.datatrap.viewmodels.SessionViewModel

class ListPrjSessionFragment : Fragment() {

    private var _binding: FragmentListPrjSessionBinding? = null
    private val binding get() = _binding!!
    private lateinit var sessionViewModel: SessionViewModel
    private lateinit var adapter: PrjSessionRecyclerAdapter
    private val args by navArgs<ListPrjSessionFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        _binding = FragmentListPrjSessionBinding.inflate(inflater, container, false)
        sessionViewModel = ViewModelProvider(this).get(SessionViewModel::class.java)

        adapter = PrjSessionRecyclerAdapter()
        val recyclerView = binding.prjSessionRecyclerview
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        sessionViewModel.getSessionsForProject(args.project.projectName).observe(viewLifecycleOwner, Observer { sessions ->
            adapter.setData(sessions)
        })

        adapter.setOnItemClickListener(object : PrjSessionRecyclerAdapter.MyClickListener{
            override fun useClickListener(position: Int) {
                // ideme do occasion
                sessionViewModel.getSessionsForProject(args.project.projectName).observe(viewLifecycleOwner, Observer { sessions ->
                    val session: Session = sessions[position]
                    val action = ListPrjSessionFragmentDirections.actionListPrjSessionFragmentToListSesOccasionFragment(session, args.locality)
                    findNavController().navigate(action)
                })
            }

            override fun useLongClickListener(position: Int) {
                // uprava vybranej session
                sessionViewModel.getSessionsForProject(args.project.projectName).observe(viewLifecycleOwner, Observer { sessions ->
                    val session: Session = sessions[position]
                    val action = ListPrjSessionFragmentDirections.actionListPrjSessionFragmentToUpdateSessionFragment(session)
                    findNavController().navigate(action)
                })
            }
        })

        binding.addSessionFloatButton.setOnClickListener {
            // pridanie novej session
            val action = ListPrjSessionFragmentDirections.actionListPrjSessionFragmentToAddSessionFragment()
            findNavController().navigate(action)
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}