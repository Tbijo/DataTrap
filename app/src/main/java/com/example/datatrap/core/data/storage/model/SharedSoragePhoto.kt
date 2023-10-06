package com.example.datatrap.core.data.storage.model

import android.net.Uri

data class SharedSoragePhoto(
    val id: Long,
    val name: String,
    val width: Int,
    val height: Int,
    val contentUri: Uri
)
