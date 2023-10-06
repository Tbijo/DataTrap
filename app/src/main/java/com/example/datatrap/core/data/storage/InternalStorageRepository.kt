package com.example.datatrap.core.data.storage

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.activity.ComponentActivity
import com.example.datatrap.core.data.storage.model.InternalStoragePhoto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

class InternalStorageRepository(
    private val context: Context,
) {
    suspend fun saveImage(fileName: String, bmp: Bitmap): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                context.openFileOutput("$fileName.jpg", ComponentActivity.MODE_PRIVATE).use { stream->
                    if (!bmp.compress(Bitmap.CompressFormat.JPEG, 95, stream)) {
                        throw IOException("Could not compress bitmap")
                    }
                }
                //val a = context.getFileStreamPath(fileName)
                true
            } catch (e: IOException) {
                e.printStackTrace()
                false
            }
        }
    }

    suspend fun getImages(): List<InternalStoragePhoto> {
        return withContext(Dispatchers.IO) {
            val files = context.filesDir.listFiles()

            files?.filter { it.canRead() && it.isFile && it.name.endsWith(".jpg") }
                ?.map {
                    val bytes = it.readBytes()
                    val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                    InternalStoragePhoto(it.name, bmp)
                } ?: listOf()
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