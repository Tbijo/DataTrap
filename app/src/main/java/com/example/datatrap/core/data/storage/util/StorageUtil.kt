package com.example.datatrap.core.data.storage.util

import android.os.Build

// pomocna funkcia ktora spusti funkciu a vrati jej vysledok ak je zariadenie API 29 alebo viac
// inak vrati null a to potom odchytit
// pomocna funkcia aby v kode nebolo treba pisat vsade if
inline fun <T> sdk29AndUp(onSdk29: ()->T): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        onSdk29()
    } else null
}