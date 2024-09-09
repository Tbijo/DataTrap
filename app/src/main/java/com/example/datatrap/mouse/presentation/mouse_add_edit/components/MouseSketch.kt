package com.example.datatrap.mouse.presentation.mouse_add_edit.components

import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.imageResource
import com.example.datatrap.R

val TisRightTopID: List<Int> = listOf(
    R.drawable.prava_predna_prvy,
    R.drawable.prava_predna_druhy,
    R.drawable.prava_predna_treti,
    R.drawable.prava_predna_stvrty,
    R.drawable.prava_predna_piaty
)
val DesRightBottomID: List<Int> = listOf(
    R.drawable.prava_zadna_prvy,
    R.drawable.prava_zadna_druhy,
    R.drawable.prava_zadna_treti,
    R.drawable.prava_zadna_stvrty,
    R.drawable.prava_zadna_piaty
)
val StoLeftTopID: List<Int> = listOf(
    R.drawable.lava_predna_prvy,
    R.drawable.lava_predna_druhy,
    R.drawable.lava_predna_treti,
    R.drawable.lava_predna_stvrty,
    R.drawable.lava_predna_piaty
)
val JedLeftBottomID: List<Int> = listOf(
    R.drawable.lava_zadna_prvy,
    R.drawable.lava_zadna_druhy,
    R.drawable.lava_zadna_treti,
    R.drawable.lava_zadna_stvrty,
    R.drawable.lava_zadna_piaty
)

@Composable
fun MouseSketch(
    uniCode: Int,
    fingers: Int,
) {
    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.White),
            contentAlignment = Alignment.CenterStart,
        ) {
            Text(text = "Code: $uniCode")
        }

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            if (fingers == 5) {
                Image(bitmap = drawAndView5(uniCode).asImageBitmap(), contentDescription = "mouse sketch")
            } else {
                Image(bitmap = drawAndView4(uniCode).asImageBitmap(), contentDescription = "mouse sketch")
            }
        }
    }
}

@Composable
private fun getImage(imageRes: Int): Bitmap {
//    BitmapFactory.decodeResource(Resources.getSystem(), imageRes)
//    return BitmapFactory.decodeResource(resources, imageRes)
    val imageBitmap: ImageBitmap = ImageBitmap.imageResource(imageRes)
    return imageBitmap.asAndroidBitmap()
}

@Composable
private fun drawAndView5(unicode: Int): Bitmap {
    val background5: ImageBitmap = ImageBitmap.imageResource(R.drawable.pat_prstov)

    val tempBitmap = Bitmap.createBitmap(background5.width, background5.height, Bitmap.Config.RGB_565)

    val tempCanvas = Canvas(tempBitmap)

    tempCanvas.drawBitmap(background5.asAndroidBitmap(), 0f, 0f, null)

    val unicodeS = unicode.toString()

    for (i in unicodeS.lastIndex downTo 0) {
        val cislo = unicodeS.substring(i, i + 1).toInt()
        when (cislo) {
            0 -> continue

            in 1..5 -> {//cislica
                when (unicodeS.length - i) {// poradie
                    1 -> tempCanvas.drawBitmap(
                        getImage(JedLeftBottomID[cislo - 1]), 0f, 0f, null
                    )
                    2 -> tempCanvas.drawBitmap(
                        getImage(DesRightBottomID[cislo - 1]), 0f, 0f, null
                    )
                    3 -> tempCanvas.drawBitmap(getImage(StoLeftTopID[cislo - 1]), 0f, 0f, null)
                    4 -> tempCanvas.drawBitmap(getImage(TisRightTopID[cislo - 1]), 0f, 0f, null)
                }
            }
            6 -> {
                when (unicodeS.length - i) {
                    1 -> {
                        tempCanvas.drawBitmap(getImage(JedLeftBottomID[0]), 0f, 0f, null)
                        tempCanvas.drawBitmap(getImage(JedLeftBottomID[4]), 0f, 0f, null)
                    }
                    2 -> {
                        tempCanvas.drawBitmap(getImage(DesRightBottomID[0]), 0f, 0f, null)
                        tempCanvas.drawBitmap(getImage(DesRightBottomID[4]), 0f, 0f, null)
                    }
                    3 -> {
                        tempCanvas.drawBitmap(getImage(StoLeftTopID[0]), 0f, 0f, null)
                        tempCanvas.drawBitmap(getImage(StoLeftTopID[4]), 0f, 0f, null)
                    }
                    4 -> {
                        tempCanvas.drawBitmap(getImage(TisRightTopID[0]), 0f, 0f, null)
                        tempCanvas.drawBitmap(getImage(TisRightTopID[4]), 0f, 0f, null)
                    }
                }
            }
            7 -> {
                when (unicodeS.length - i) {
                    1 -> {
                        tempCanvas.drawBitmap(getImage(JedLeftBottomID[1]), 0f, 0f, null)
                        tempCanvas.drawBitmap(getImage(JedLeftBottomID[4]), 0f, 0f, null)
                    }
                    2 -> {
                        tempCanvas.drawBitmap(getImage(DesRightBottomID[1]), 0f, 0f, null)
                        tempCanvas.drawBitmap(getImage(DesRightBottomID[4]), 0f, 0f, null)
                    }
                    3 -> {
                        tempCanvas.drawBitmap(getImage(StoLeftTopID[1]), 0f, 0f, null)
                        tempCanvas.drawBitmap(getImage(StoLeftTopID[4]), 0f, 0f, null)
                    }
                    4 -> {
                        tempCanvas.drawBitmap(getImage(TisRightTopID[1]), 0f, 0f, null)
                        tempCanvas.drawBitmap(getImage(TisRightTopID[4]), 0f, 0f, null)
                    }
                }
            }
            8 -> {
                when (unicodeS.length - i) {
                    1 -> {
                        tempCanvas.drawBitmap(getImage(JedLeftBottomID[2]), 0f, 0f, null)
                        tempCanvas.drawBitmap(getImage(JedLeftBottomID[4]), 0f, 0f, null)
                    }
                    2 -> {
                        tempCanvas.drawBitmap(getImage(DesRightBottomID[2]), 0f, 0f, null)
                        tempCanvas.drawBitmap(getImage(DesRightBottomID[4]), 0f, 0f, null)
                    }
                    3 -> {
                        tempCanvas.drawBitmap(getImage(StoLeftTopID[2]), 0f, 0f, null)
                        tempCanvas.drawBitmap(getImage(StoLeftTopID[4]), 0f, 0f, null)
                    }
                    4 -> {
                        tempCanvas.drawBitmap(getImage(TisRightTopID[2]), 0f, 0f, null)
                        tempCanvas.drawBitmap(getImage(TisRightTopID[4]), 0f, 0f, null)
                    }
                }
            }
            9 -> {
                when (unicodeS.length - i) {
                    1 -> {
                        tempCanvas.drawBitmap(getImage(JedLeftBottomID[3]), 0f, 0f, null)
                        tempCanvas.drawBitmap(getImage(JedLeftBottomID[4]), 0f, 0f, null)
                    }
                    2 -> {
                        tempCanvas.drawBitmap(getImage(DesRightBottomID[3]), 0f, 0f, null)
                        tempCanvas.drawBitmap(getImage(DesRightBottomID[4]), 0f, 0f, null)
                    }
                    3 -> {
                        tempCanvas.drawBitmap(getImage(StoLeftTopID[3]), 0f, 0f, null)
                        tempCanvas.drawBitmap(getImage(StoLeftTopID[4]), 0f, 0f, null)
                    }
                    4 -> {
                        tempCanvas.drawBitmap(getImage(TisRightTopID[3]), 0f, 0f, null)
                        tempCanvas.drawBitmap(getImage(TisRightTopID[4]), 0f, 0f, null)
                    }
                }
            }
        }
    }
    //imgView.setImageBitmap(tempBitmap)
    return tempBitmap
}

@Composable
private fun drawAndView4(unicode: Int): Bitmap {

    val background4: ImageBitmap = ImageBitmap.imageResource(R.drawable.styri_prsty)

    val tempBitmap = Bitmap.createBitmap(background4.width, background4.height, Bitmap.Config.RGB_565)

    val tempCanvas = Canvas(tempBitmap)

    tempCanvas.drawBitmap(background4.asAndroidBitmap(), 0f, 0f, null)

    val unicodeS = unicode.toString()

    for (i in unicodeS.lastIndex downTo 0) {
        val cislo = unicodeS.substring(i, i + 1).toInt()

        when (cislo) {
            0 -> continue

            in 1..4 -> {
                when (unicodeS.length - i) {
                    1 -> tempCanvas.drawBitmap(
                        getImage(JedLeftBottomID[cislo - 1]), 0f, 0f, null
                    )
                    2 -> tempCanvas.drawBitmap(
                        getImage(DesRightBottomID[cislo - 1]), 0f, 0f, null
                    )
                    3 -> tempCanvas.drawBitmap(getImage(StoLeftTopID[cislo - 1]), 0f, 0f, null)
                    4 -> tempCanvas.drawBitmap(getImage(TisRightTopID[cislo - 1]), 0f, 0f, null)
                }
            }
            5 -> {
                when (unicodeS.length - i) {
                    1 -> {
                        tempCanvas.drawBitmap(
                            getImage(JedLeftBottomID[cislo - 1]), 0f, 0f, null
                        )
                    }
                    2 -> {
                        tempCanvas.drawBitmap(
                            getImage(DesRightBottomID[cislo - 1]), 0f, 0f, null
                        )
                    }
                    3 -> {
                        tempCanvas.drawBitmap(getImage(StoLeftTopID[0]), 0f, 0f, null)
                        tempCanvas.drawBitmap(getImage(StoLeftTopID[3]), 0f, 0f, null)
                    }
                    4 -> {
                        tempCanvas.drawBitmap(getImage(TisRightTopID[0]), 0f, 0f, null)
                        tempCanvas.drawBitmap(getImage(TisRightTopID[3]), 0f, 0f, null)
                    }
                }
            }
            6 -> {
                when (unicodeS.length - i) {
                    1 -> {
                        tempCanvas.drawBitmap(getImage(JedLeftBottomID[0]), 0f, 0f, null)
                        tempCanvas.drawBitmap(getImage(JedLeftBottomID[4]), 0f, 0f, null)
                    }
                    2 -> {
                        tempCanvas.drawBitmap(getImage(DesRightBottomID[0]), 0f, 0f, null)
                        tempCanvas.drawBitmap(getImage(DesRightBottomID[4]), 0f, 0f, null)
                    }
                    3 -> {
                        tempCanvas.drawBitmap(getImage(StoLeftTopID[1]), 0f, 0f, null)
                        tempCanvas.drawBitmap(getImage(StoLeftTopID[3]), 0f, 0f, null)
                    }
                    4 -> {
                        tempCanvas.drawBitmap(getImage(TisRightTopID[1]), 0f, 0f, null)
                        tempCanvas.drawBitmap(getImage(TisRightTopID[3]), 0f, 0f, null)
                    }
                }
            }
            7 -> {
                when (unicodeS.length - i) {
                    1 -> {
                        tempCanvas.drawBitmap(getImage(JedLeftBottomID[1]), 0f, 0f, null)
                        tempCanvas.drawBitmap(getImage(JedLeftBottomID[4]), 0f, 0f, null)
                    }
                    2 -> {
                        tempCanvas.drawBitmap(getImage(DesRightBottomID[1]), 0f, 0f, null)
                        tempCanvas.drawBitmap(getImage(DesRightBottomID[4]), 0f, 0f, null)
                    }
                    3 -> {
                        tempCanvas.drawBitmap(getImage(StoLeftTopID[2]), 0f, 0f, null)
                        tempCanvas.drawBitmap(getImage(StoLeftTopID[3]), 0f, 0f, null)
                    }
                    4 -> {
                        tempCanvas.drawBitmap(getImage(TisRightTopID[2]), 0f, 0f, null)
                        tempCanvas.drawBitmap(getImage(TisRightTopID[3]), 0f, 0f, null)
                    }
                }
            }
            8 -> {
                when (unicodeS.length - i) {
                    1 -> {
                        tempCanvas.drawBitmap(getImage(JedLeftBottomID[2]), 0f, 0f, null)
                        tempCanvas.drawBitmap(getImage(JedLeftBottomID[4]), 0f, 0f, null)
                    }
                    2 -> {
                        tempCanvas.drawBitmap(getImage(DesRightBottomID[2]), 0f, 0f, null)
                        tempCanvas.drawBitmap(getImage(DesRightBottomID[4]), 0f, 0f, null)
                    }
                    3 -> {
                        tempCanvas.drawBitmap(getImage(StoLeftTopID[0]), 0f, 0f, null)
                        tempCanvas.drawBitmap(getImage(StoLeftTopID[2]), 0f, 0f, null)
                        tempCanvas.drawBitmap(getImage(StoLeftTopID[3]), 0f, 0f, null)
                    }
                    4 -> {
                        tempCanvas.drawBitmap(getImage(TisRightTopID[0]), 0f, 0f, null)
                        tempCanvas.drawBitmap(getImage(TisRightTopID[2]), 0f, 0f, null)
                        tempCanvas.drawBitmap(getImage(TisRightTopID[3]), 0f, 0f, null)
                    }
                }
            }
            9 -> {
                when (unicodeS.length - i) {
                    1 -> {
                        tempCanvas.drawBitmap(getImage(JedLeftBottomID[3]), 0f, 0f, null)
                        tempCanvas.drawBitmap(getImage(JedLeftBottomID[4]), 0f, 0f, null)
                    }
                    2 -> {
                        tempCanvas.drawBitmap(getImage(DesRightBottomID[3]), 0f, 0f, null)
                        tempCanvas.drawBitmap(getImage(DesRightBottomID[4]), 0f, 0f, null)
                    }
                    3 -> {
                        tempCanvas.drawBitmap(getImage(StoLeftTopID[1]), 0f, 0f, null)
                        tempCanvas.drawBitmap(getImage(StoLeftTopID[2]), 0f, 0f, null)
                        tempCanvas.drawBitmap(getImage(StoLeftTopID[3]), 0f, 0f, null)
                    }
                    4 -> {
                        tempCanvas.drawBitmap(getImage(TisRightTopID[1]), 0f, 0f, null)
                        tempCanvas.drawBitmap(getImage(TisRightTopID[2]), 0f, 0f, null)
                        tempCanvas.drawBitmap(getImage(TisRightTopID[3]), 0f, 0f, null)
                    }
                }
            }
        }
    }

    //imgView.setImageBitmap(tempBitmap)
    return tempBitmap
}