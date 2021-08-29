package com.example.datatrap.specie.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.datatrap.databinding.FragmentViewSpecieBinding
import com.example.datatrap.models.Picture
import com.example.datatrap.specie.fragments.list.ViewSpecieFragmentArgs
import com.example.datatrap.viewmodels.PictureViewModel

class ViewSpecieFragment : Fragment() {

    private var _binding: FragmentViewSpecieBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<ViewSpecieFragmentArgs>()
    private lateinit var pictureViewModel: PictureViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        _binding = FragmentViewSpecieBinding.inflate(inflater, container, false)
        pictureViewModel = ViewModelProvider(this).get(PictureViewModel::class.java)

        if (args.specie.imgName == null){
            args.specie.imgName?.let {
                val picture: Picture? = pictureViewModel.getPictureById(it).value
                binding.ivPicture.setImageURI(picture?.path?.toUri())
            }
        }

        binding.tvAuthority.text = args.specie.authority
        binding.tvDescription.text = args.specie.description
        binding.tvFullName.text = args.specie.fullName
        binding.tvMaxWeight.text = args.specie.maxWeight.toString()
        binding.tvMinWeight.text = args.specie.minWeight.toString()
        binding.tvNumberUpperFinger.text = args.specie.upperFingers.toString()
        binding.tvSpecieCodeView.text = args.specie.speciesCode
        binding.tvSynonymum.text = args.specie.synonym

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}