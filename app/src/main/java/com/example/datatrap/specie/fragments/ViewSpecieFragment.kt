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

        if (args.specie.imgName != null){
            pictureViewModel.getPictureById(args.specie.imgName!!)
        }
        pictureViewModel.gotPicture.observe(viewLifecycleOwner, {
            binding.ivPicture.setImageURI(it.path.toUri())
        })

        initSpecieValuesToView()

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun initSpecieValuesToView(){
        binding.tvSpecieCodeView.text = args.specie.speciesCode
        binding.tvFullName.text = args.specie.fullName
        binding.tvAuthority.text = args.specie.authority.toString()
        binding.tvDescription.text = args.specie.description.toString()
        binding.tvMaxWeight.text = args.specie.maxWeight.toString()
        binding.tvMinWeight.text = args.specie.minWeight.toString()
        binding.tvNumberUpperFinger.text = args.specie.upperFingers.toString()
        binding.tvSynonymum.text = args.specie.synonym.toString()

        binding.tvBody.text = args.specie.bodyLength.toString()
        binding.tvTail.text = args.specie.tailLength.toString()
        binding.tvMinFeet.text = args.specie.feetLengthMin.toString()
        binding.tvMaxFeet.text = args.specie.feetLengthMax.toString()
        binding.tvSpecieNote.text = args.specie.note.toString()
    }

}