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
import com.example.datatrap.models.Specie
import com.example.datatrap.viewmodels.PictureViewModel
import com.example.datatrap.viewmodels.SpecieViewModel

class ViewSpecieFragment : Fragment() {

    private var _binding: FragmentViewSpecieBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<ViewSpecieFragmentArgs>()
    private lateinit var pictureViewModel: PictureViewModel
    private lateinit var specieViewModel: SpecieViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        _binding = FragmentViewSpecieBinding.inflate(inflater, container, false)
        specieViewModel = ViewModelProvider(this).get(SpecieViewModel::class.java)
        pictureViewModel = ViewModelProvider(this).get(PictureViewModel::class.java)

        if (args.specList.imgName != null){
            pictureViewModel.getPictureById(args.specList.imgName!!)
        }
        pictureViewModel.gotPicture.observe(viewLifecycleOwner, {
            binding.ivPicture.setImageURI(it.path.toUri())
        })

        specieViewModel.getSpecie(args.specList.specieId).observe(viewLifecycleOwner, {
            initSpecieValuesToView(it)
        })

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun initSpecieValuesToView(specie: Specie){
        binding.tvSpecieCodeView.text = specie.speciesCode
        binding.tvFullName.text = specie.fullName
        binding.tvAuthority.text = specie.authority.toString()
        binding.tvDescription.text = specie.description.toString()
        binding.tvMaxWeight.text = specie.maxWeight.toString()
        binding.tvMinWeight.text = specie.minWeight.toString()
        binding.tvNumberUpperFinger.text = specie.upperFingers.toString()
        binding.tvSynonymum.text = specie.synonym.toString()

        binding.tvBody.text = specie.bodyLength.toString()
        binding.tvTail.text = specie.tailLength.toString()
        binding.tvMinFeet.text = specie.feetLengthMin.toString()
        binding.tvMaxFeet.text = specie.feetLengthMax.toString()
        binding.tvSpecieNote.text = specie.note.toString()
    }

}