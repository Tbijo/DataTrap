package com.example.datatrap.bugreport

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.datatrap.R
import com.example.datatrap.databinding.FragmentAboutBinding
import com.example.datatrap.databinding.FragmentBugReportBinding

class BugReportFragment : Fragment() {

    private var _binding: FragmentBugReportBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        _binding = FragmentBugReportBinding.inflate(inflater, container, false)

        binding.textView.text = "Bug Report Activity"

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}