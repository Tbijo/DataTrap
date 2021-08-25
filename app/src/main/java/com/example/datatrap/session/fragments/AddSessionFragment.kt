package com.example.datatrap.session.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.datatrap.databinding.FragmentAddSessionBinding
import com.example.datatrap.viewmodels.SessionViewModel

class AddSessionFragment : Fragment() {

    private var _binding: FragmentAddSessionBinding? = null
    private val binding get() = _binding!!
    private lateinit var sessionViewModel: SessionViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        _binding = FragmentAddSessionBinding.inflate(inflater, container, false)

        sessionViewModel = ViewModelProvider(this).get(SessionViewModel::class.java)

        binding.btnAddSession.setOnClickListener {
            insertSession()
        }

        return binding.root
    }

    private fun insertSession() {

    }

}