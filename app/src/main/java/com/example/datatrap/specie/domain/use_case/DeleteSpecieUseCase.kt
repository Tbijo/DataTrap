package com.example.datatrap.specie.domain.use_case

import com.example.datatrap.specie.data.SpecieRepository
import com.example.datatrap.specie.data.specie_image.SpecieImageRepository

class DeleteSpecieUseCase(
    private val specieRepository: SpecieRepository,
    private val specieImageRepository: SpecieImageRepository,
) {
    // TODO Remove from Storage as well Occasion and Mouse too
    suspend operator fun invoke(specieId: String) {
        val specieImage = specieImageRepository.getImageForSpecie(specieId)

        specieImage?.let {
            specieImageRepository.deleteImage(specieImage)
        }

        val specie = specieRepository.getSpecie(specieId)

        specieRepository.deleteSpecie(specie)
    }
}