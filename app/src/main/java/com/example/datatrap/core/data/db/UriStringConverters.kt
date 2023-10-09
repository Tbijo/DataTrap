package com.example.datatrap.core.data.db

import android.net.Uri
import androidx.room.TypeConverter

class UriStringConverters {
    @TypeConverter
    fun fromUriToString(imageUri: Uri): String {
        return imageUri.toString()

    }

    @TypeConverter
    fun fromStringToUri(uriStr: String): Uri {
        return Uri.parse(uriStr)
    }
}