package com.example.datatrap.mouse.presentation.mouse_add_edit_recap.components

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.widget.ImageView
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.asImageBitmap
import com.example.datatrap.R

@Composable
fun MouseSketch(uniCode: Int, fingers: Int) {

    lateinit var TisRightTopID: List<Int>
    lateinit var DesRightBottomID: List<Int>
    lateinit var StoLeftTopID: List<Int>
    lateinit var JedLeftBottomID: List<Int>

    callResources()

    if (fingers == 5) {
        //drawAndView5(binding.mainImage, uniCode)
        Image(bitmap = BitmapFactory.decodeFile("").asImageBitmap(), contentDescription = null)
    } else {
        //drawAndView4(binding.mainImage, uniCode)
        Image(bitmap = BitmapFactory.decodeFile("").asImageBitmap(), contentDescription = null)
    }

}

private fun callResources() {
    TisRightTopID = listOf(
        R.drawable.prava_predna_prvy,
        R.drawable.prava_predna_druhy,
        R.drawable.prava_predna_treti,
        R.drawable.prava_predna_stvrty,
        R.drawable.prava_predna_piaty
    )
    DesRightBottomID = listOf(
        R.drawable.prava_zadna_prvy,
        R.drawable.prava_zadna_druhy,
        R.drawable.prava_zadna_treti,
        R.drawable.prava_zadna_stvrty,
        R.drawable.prava_zadna_piaty
    )
    StoLeftTopID = listOf(
        R.drawable.lava_predna_prvy,
        R.drawable.lava_predna_druhy,
        R.drawable.lava_predna_treti,
        R.drawable.lava_predna_stvrty,
        R.drawable.lava_predna_piaty
    )
    JedLeftBottomID = listOf(
        R.drawable.lava_zadna_prvy,
        R.drawable.lava_zadna_druhy,
        R.drawable.lava_zadna_treti,
        R.drawable.lava_zadna_stvrty,
        R.drawable.lava_zadna_piaty
    )
}

private fun getImage(imageRes: Int): Bitmap {
    return BitmapFactory.decodeResource(resources, imageRes)
}

private fun drawAndView5(imgView: ImageView, unicode: Int) {
    val background5: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.pat_prstov)
    val tempBitmap =
        Bitmap.createBitmap(background5.width, background5.height, Bitmap.Config.RGB_565)
    val tempCanvas = Canvas(tempBitmap)

    tempCanvas.drawBitmap(background5, 0f, 0f, null)

    val unicodeS = unicode.toString()

    for (i in unicodeS.lastIndex downTo 0) {
        val cislo = unicodeS.substring(i, i + 1).toInt()
        when (cislo) {
            0 -> continue

            in 1..5 -> {//cislica
                when (unicodeS.length - i) {// poradie
                    1 -> tempCanvas.drawBitmap(
                        getImage(JedLeftBottomID[cislo - 1]),
                        0f,
                        0f,
                        null
                    )
                    2 -> tempCanvas.drawBitmap(
                        getImage(DesRightBottomID[cislo - 1]),
                        0f,
                        0f,
                        null
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

    imgView.setImageBitmap(tempBitmap)
}

private fun drawAndView4(imgView: ImageView, unicode: Int) {
    val background4: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.styri_prsty)
    val tempBitmap =
        Bitmap.createBitmap(background4.width, background4.height, Bitmap.Config.RGB_565)
    val tempCanvas = Canvas(tempBitmap)

    tempCanvas.drawBitmap(background4, 0f, 0f, null)

    val unicodeS = unicode.toString()

    for (i in unicodeS.lastIndex downTo 0) {
        val cislo = unicodeS.substring(i, i + 1).toInt()

        when (cislo) {
            0 -> continue

            in 1..4 -> {
                when (unicodeS.length - i) {
                    1 -> tempCanvas.drawBitmap(
                        getImage(JedLeftBottomID[cislo - 1]),
                        0f,
                        0f,
                        null
                    )
                    2 -> tempCanvas.drawBitmap(
                        getImage(DesRightBottomID[cislo - 1]),
                        0f,
                        0f,
                        null
                    )
                    3 -> tempCanvas.drawBitmap(getImage(StoLeftTopID[cislo - 1]), 0f, 0f, null)
                    4 -> tempCanvas.drawBitmap(getImage(TisRightTopID[cislo - 1]), 0f, 0f, null)
                }
            }
            5 -> {
                when (unicodeS.length - i) {
                    1 -> {
                        tempCanvas.drawBitmap(
                            getImage(JedLeftBottomID[cislo - 1]),
                            0f,
                            0f,
                            null
                        )
                    }
                    2 -> {
                        tempCanvas.drawBitmap(
                            getImage(DesRightBottomID[cislo - 1]),
                            0f,
                            0f,
                            null
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

    imgView.setImageBitmap(tempBitmap)
}