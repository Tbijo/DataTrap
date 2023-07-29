package com.example.datatrap.specie.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SpecList(
    var specieId: Long,

    var speciesCode: String,

    var fullName: String

): Parcelable
