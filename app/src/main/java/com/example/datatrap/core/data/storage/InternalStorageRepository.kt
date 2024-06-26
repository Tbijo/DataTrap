package com.example.datatrap.core.data.storage

import android.content.Context
import android.graphics.Bitmap
import androidx.activity.ComponentActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException

class InternalStorageRepository(
    private val context: Context,
) {
    suspend fun saveImage(fileName: String, bmp: Bitmap): File? {
        return withContext(Dispatchers.IO) {
            try {
                context.openFileOutput("$fileName.jpg", ComponentActivity.MODE_PRIVATE).use { stream->
                    if (!bmp.compress(Bitmap.CompressFormat.JPEG, 95, stream)) {
                        throw IOException("Could not compress bitmap")
                    }
                }
                val file = context.getFileStreamPath("$fileName.jpg")
                file
            } catch (e: IOException) {
                e.printStackTrace()
                null
            }
        }
    }

    suspend fun getImagePath(imageName: String): String? {
        return withContext(Dispatchers.IO) {
            val files = context.filesDir.listFiles()

            files
                ?.first {
                    it.canRead() && it.isFile && it.name.endsWith(".jpg") && it.name.equals(imageName)
                }
                ?.path

        }
    }

    suspend fun deleteImage(fileName: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                context.deleteFile(fileName)
            } catch (e: IOException) {
                e.printStackTrace()
                false
            }
        }
    }
}