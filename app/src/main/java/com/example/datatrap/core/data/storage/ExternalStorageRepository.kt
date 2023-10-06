package com.example.datatrap.core.data.storage

import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.provider.MediaStore
import com.example.datatrap.core.data.storage.model.SharedSoragePhoto
import com.example.datatrap.core.data.storage.util.sdk29AndUp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

class ExternalStorageRepository(
    private val context: Context,
) {
    // MediaStore - database of media files and coresponding meta-data
    // Uri is the address where we will save our file in MediaStore
    suspend fun saveImage(displayName: String, bmp: Bitmap): Boolean {
        return withContext(Dispatchers.IO) {
            // separate VOLUME/pamat for Android below and above API 29
            val imageCollection = sdk29AndUp { // alebo sa vykona lambda alebo sa vrati null
                // sdk >= 29
                // ak mame API nad alebo rovne 29 treba vybrat toto ulozisko
                MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY) // Daju sa bybrat aj videa MediaStore.Videos. ...
                // ak mame API mensie ako 29 treba vybrat toto ulozisko
            } ?: MediaStore.Images.Media.EXTERNAL_CONTENT_URI

            // teraz mame vybrany volume ktory bude treba
            // Dalej treba vytvorit metada k bitmapu ktory chceme ulozit
            val contentValues = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, "$displayName.jpg")
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                put(MediaStore.Images.Media.WIDTH, bmp.width)
                put(MediaStore.Images.Media.HEIGHT, bmp.height)
            }

            // na ukladanie aj citanie do/z external storage sa pouziva contentResolver
            try {
                // imageCollection je vlastne Uri kde sa ulozi subor
                context.contentResolver.insert(imageCollection, contentValues)?.also { uri ->
                    // tu ziskam uri k obrazku ktory chcem ulozit
                    // insert len vytvori ENTRY s meta-datami, neulozi este image
                    // vracia teda len miesto kam mozeme image ulozit

                    // vlozenie image do Uri
                    context.contentResolver.openOutputStream(uri).use { outputStream->
                        // dalej funguje ako pri internal save
                        if (bmp.compress(Bitmap.CompressFormat.JPEG, 95, outputStream)) {
                            // ak sa nepodarilo kompresovat
                            throw IOException("Unable to compress")
                        }
                    }
                } ?: throw IOException("Unable to create MediaStore entry")
                true
            } catch (e: IOException) {
                e.printStackTrace()
                false
            }
        }
    }

    suspend fun getImages(): List<SharedSoragePhoto> {
        return withContext(Dispatchers.IO) {

            // kde budeme hladat
            val collection = sdk29AndUp {
                // ak API nad alebo rovne 29 tak tu
                MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
                // ak API menej ako 29 tak tu
            } ?: MediaStore.Images.Media.EXTERNAL_CONTENT_URI

            // ktore atributy/meta data chceme
            val projection = arrayOf(
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.WIDTH,
                MediaStore.Images.Media.HEIGHT
            )

            // fotky ktore vratime
            val photos = mutableListOf<SharedSoragePhoto>()

            // collection/volume pouzijeme v query
            context.contentResolver.query(
                collection,   // From - kde
                projection,    // Select - teda ktore atributy chceme
                null,     //(selection) Where - napr. chceme fotky ktore maju width viac ako 500px
                null, // (selectionArgs) - nase hodnoty podla ktorych hladame cize tych 500
                "${MediaStore.Images.Media.DISPLAY_NAME} ASC", // podla coho zoradit
            )?.use { cursor ->
                // dostanem referenciu na kurzor ako v databaze
                // cursor sa pouzije na pohyb/iteraciu po datach ktore nam pridu ako vysledok
                // ziskam stlpce z kazdej fotky ktore pride z nasej query
                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                val displayNameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
                val widthColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.WIDTH)
                val heightColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.HEIGHT)

                // teraz budeme iterovat po datach a vyberat udaje zo stlpcov
                while (cursor.moveToNext()) {
                    // meta data
                    val id = cursor.getLong(idColumn)
                    val displayName = cursor.getString(displayNameColumn)
                    val width = cursor.getInt(widthColumn)
                    val height = cursor.getInt(heightColumn)

                    // treba ziskat aj byty nasej fotky
                    // byty ziskame cez Uri
                    val contentUri = ContentUris.withAppendedId(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        id // id fotky ktorej Uri chceme
                    )

                    val photo = SharedSoragePhoto(id, displayName, width, height, contentUri)
                    photos.add(photo)
                }
                photos.toList()
            } ?: emptyList()
        }
    }
}