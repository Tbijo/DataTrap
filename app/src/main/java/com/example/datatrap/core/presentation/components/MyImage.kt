package com.example.datatrap.core.presentation.components

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import com.example.datatrap.R
import java.io.File

@Composable
fun MyImage(
    modifier: Modifier = Modifier,
    imagePath: String?,
    contentDescription: String,
    onClick: () -> Unit,
) {
    imagePath?.let {
        // on below line we are creating an image file and
        // specifying path for the image file on below line.
        val imgFile = File(imagePath)

        // on below line we are checking if the image file exist or not.
        var imgBitmap: Bitmap? = null
        if (imgFile.exists()) {
            // on below line we are creating an image bitmap variable
            // and adding a bitmap to it from image file.
            imgBitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
        }
        imgBitmap?.let {
            Image(
                modifier = modifier.clickable { onClick() },
                bitmap = imgBitmap.asImageBitmap(),
                contentDescription = contentDescription,
            )
        }
    } ?: Image(
        modifier = modifier,
        painter = painterResource(id = R.drawable.empty),
        contentDescription = contentDescription,
    )
}

@Composable
fun MyImage(
    modifier: Modifier = Modifier,
    imgBitmap: Bitmap?,
    contentDescription: String,
    onClick: () -> Unit,
) {
    imgBitmap?.let {
        Image(
            modifier = modifier.clickable { onClick() },
            bitmap = imgBitmap.asImageBitmap(),
            contentDescription = contentDescription,
        )
    } ?: Image(
        modifier = modifier,
        painter = painterResource(id = R.drawable.empty),
        contentDescription = contentDescription,
    )
}