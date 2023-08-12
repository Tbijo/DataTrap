package com.example.datatrap.specie.presentation.specie_detail

import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import com.example.datatrap.core.presentation.components.ViewImageFragment
import com.example.datatrap.specie.presentation.specie_image.SpecieImageViewModel
import com.example.datatrap.specie.data.Specie
import com.example.datatrap.specie.presentation.specie_list.SpecieListViewModel

class SpecieDetailViewModel: ViewModel() {

    private val specieListViewModel: SpecieListViewModel by viewModels()
    private val specieImageViewModel: SpecieImageViewModel by viewModels()
    private var path: String? = null

    init {
        specieImageViewModel.getImageForSpecie(args.specList.specieId).observe(viewLifecycleOwner) {
            if (it != null) {
                binding.ivPicture.setImageURI(it.path.toUri())
                path = it.path
            }
        }

        specieListViewModel.getSpecie(args.specList.specieId).observe(viewLifecycleOwner) {
            initSpecieValuesToView(it)
        }

        binding.ivPicture.setOnClickListener {
            if (path != null) {
                val fragman = requireActivity().supportFragmentManager
                val floatFrag = ViewImageFragment(path!!)
                floatFrag.show(fragman, "FloatFragViewImage")
            }
        }
    }

    private fun initSpecieValuesToView(specie: Specie) {
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