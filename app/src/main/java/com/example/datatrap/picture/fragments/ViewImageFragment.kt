package com.example.datatrap.picture.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.DialogFragment
import com.example.datatrap.databinding.FragmentViewImageBinding

class ViewImageFragment(private val uriString: String) : DialogFragment() {

    private var _binding: FragmentViewImageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        _binding = FragmentViewImageBinding.inflate(inflater, container, false)

        binding.ivSelectImage.setImageURI(uriString.toUri())

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}