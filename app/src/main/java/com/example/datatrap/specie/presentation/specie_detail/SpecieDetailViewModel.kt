package com.example.datatrap.specie.presentation.specie_detail

import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import com.example.datatrap.core.presentation.components.ViewImageFragment
import com.example.datatrap.specie.presentation.specie_image.SpecieImageViewModel
import com.example.datatrap.specie.data.SpecieEntity
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

    private fun initSpecieValuesToView(specieEntity: SpecieEntity) {
        binding.tvSpecieCodeView.text = specieEntity.speciesCode
        binding.tvFullName.text = specieEntity.fullName
        binding.tvAuthority.text = specieEntity.authority.toString()
        binding.tvDescription.text = specieEntity.description.toString()
        binding.tvMaxWeight.text = specieEntity.maxWeight.toString()
        binding.tvMinWeight.text = specieEntity.minWeight.toString()
        binding.tvNumberUpperFinger.text = specieEntity.upperFingers.toString()
        binding.tvSynonymum.text = specieEntity.synonym.toString()

        binding.tvBody.text = specieEntity.bodyLength.toString()
        binding.tvTail.text = specieEntity.tailLength.toString()
        binding.tvMinFeet.text = specieEntity.feetLengthMin.toString()
        binding.tvMaxFeet.text = specieEntity.feetLengthMax.toString()
        binding.tvSpecieNote.text = specieEntity.note.toString()
    }
}