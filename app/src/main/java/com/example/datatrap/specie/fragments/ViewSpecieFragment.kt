package com.example.datatrap.specie.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.datatrap.databinding.FragmentViewSpecieBinding
import com.example.datatrap.models.Picture
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

        initSpecieValuesToView()

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun initSpecieValuesToView(){
        if (args.specie.imgName != null){
            val picture: Picture = pictureViewModel.getPictureById(args.specie.imgName!!)
            binding.ivPicture.setImageURI(picture.path.toUri())
            Toast.makeText(requireContext(), "Fotka je", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "Fotka nie je", Toast.LENGTH_SHORT).show()
        }

        binding.tvAuthority.text = args.specie.authority
        binding.tvDescription.text = args.specie.description
        binding.tvFullName.text = args.specie.fullName
        binding.tvMaxWeight.text = args.specie.maxWeight.toString()
        binding.tvMinWeight.text = args.specie.minWeight.toString()
        binding.tvNumberUpperFinger.text = args.specie.upperFingers.toString()
        binding.tvSpecieCodeView.text = args.specie.speciesCode
        binding.tvSynonymum.text = args.specie.synonym
    }

}