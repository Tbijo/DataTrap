package com.example.datatrap.session.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.datatrap.R
import com.example.datatrap.databinding.FragmentAddSessionBinding
import com.example.datatrap.databinding.FragmentUpdateSessionBinding
import com.example.datatrap.viewmodels.SessionViewModel

class UpdateSessionFragment : Fragment() {

    private var _binding: FragmentUpdateSessionBinding? = null
    private val binding get() = _binding!!
    private lateinit var sessionViewModel: SessionViewModel
    private val args by navArgs<UpdateSessionFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        _binding = FragmentUpdateSessionBinding.inflate(inflater, container, false)
        sessionViewModel = ViewModelProvider(this).get(SessionViewModel::class.java)

        return binding.root
    }

}