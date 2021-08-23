package com.example.datatrap.specie.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.datatrap.databinding.FragmentAddSpecieBinding
import com.example.datatrap.models.Specie
import com.example.datatrap.viewmodels.SpecieViewModel

class AddSpecieFragment : Fragment() {

    private var _binding: FragmentAddSpecieBinding? = null
    private val binding get() = _binding!!
    private lateinit var specieViewModel: SpecieViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        _binding = FragmentAddSpecieBinding.inflate(inflater, container, false)
        specieViewModel = ViewModelProvider(this).get(SpecieViewModel::class.java)

        binding.btnAddSpecie.setOnClickListener {
            insertSpecie()
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun insertSpecie() {
        val speciesCode = ""
        val fullName = ""
        val synonym = null
        val authority = ""
        val description = null
        val isSmallMammal = 0
        val upperFingers = null
        val minWeight = null
        val maxWeight = null
        val note = null
        val img = null

        val specie = Specie(speciesCode, fullName, synonym, authority, description, isSmallMammal, upperFingers, minWeight, maxWeight, note, img)
    }

}