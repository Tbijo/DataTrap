package com.example.datatrap.about

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.datatrap.R
import com.example.datatrap.databinding.ActivityAboutBinding
import com.example.datatrap.databinding.FragmentAboutBinding
import com.example.datatrap.databinding.FragmentAddProjectBinding

class AboutFragment : Fragment() {

    private var _binding: FragmentAboutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        _binding = FragmentAboutBinding.inflate(inflater, container, false)

        binding.textView.text = "About Activity"

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}