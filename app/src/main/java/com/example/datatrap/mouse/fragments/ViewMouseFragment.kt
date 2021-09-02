package com.example.datatrap.mouse.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.datatrap.databinding.FragmentViewMouseBinding

class ViewMouseFragment : Fragment() {

    private var _binding: FragmentViewMouseBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<ViewMouseFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        _binding = FragmentViewMouseBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}