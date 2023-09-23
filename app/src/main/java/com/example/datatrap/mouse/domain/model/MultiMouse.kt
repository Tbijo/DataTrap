package com.example.datatrap.mouse.domain.model

import com.example.datatrap.specie.data.SpecieEntity

data class MultiMouse(
    var trapID: Int = 0,
    var specie: SpecieEntity? = null,
    var isTrapIdExpanded: Boolean = false,
    var isSpecieExpanded: Boolean = false,
)
