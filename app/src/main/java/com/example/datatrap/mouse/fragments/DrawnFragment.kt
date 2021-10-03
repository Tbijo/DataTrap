package com.example.datatrap.mouse.fragments

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import com.example.datatrap.R
import com.example.datatrap.databinding.FragmentDrawnBinding

class DrawnFragment(private val uniCode: Int, private val fingers: Int) : DialogFragment() {

    private var _binding: FragmentDrawnBinding? = null
    private val binding get() = _binding!!

    private lateinit var background5: Bitmap
    private lateinit var background4: Bitmap

    private lateinit var TisRightTopImgs: List<Bitmap>
    private lateinit var DesRightBottomImgs: List<Bitmap>
    private lateinit var StoLeftTopImgs: List<Bitmap>
    private lateinit var JedLeftBottomImgs: List<Bitmap>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDrawnBinding.inflate(inflater, container, false)

        callResources()

        if (fingers == 5) {
            drawAndView5(binding.mainImage, background5, uniCode)
        } else {
            drawAndView4(binding.mainImage, background4, uniCode)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun callResources() {
        background5 = BitmapFactory.decodeResource(resources, R.drawable.pat_prstov)
        background4 = BitmapFactory.decodeResource(resources, R.drawable.styri_prsty)
        TisRightTopImgs = listOf(
            BitmapFactory.decodeResource(resources, R.drawable.prava_predna_prvy),
            BitmapFactory.decodeResource(resources, R.drawable.prava_predna_druhy),
            BitmapFactory.decodeResource(resources, R.drawable.prava_predna_treti),
            BitmapFactory.decodeResource(resources, R.drawable.prava_predna_stvrty),
            BitmapFactory.decodeResource(resources, R.drawable.prava_predna_piaty)
        )
        DesRightBottomImgs = listOf(
            BitmapFactory.decodeResource(resources, R.drawable.prava_zadna_prvy),
            BitmapFactory.decodeResource(resources, R.drawable.prava_zadna_druhy),
            BitmapFactory.decodeResource(resources, R.drawable.prava_zadna_treti),
            BitmapFactory.decodeResource(resources, R.drawable.prava_zadna_stvrty),
            BitmapFactory.decodeResource(resources, R.drawable.prava_zadna_piaty)
        )
        StoLeftTopImgs = listOf(
            BitmapFactory.decodeResource(resources, R.drawable.lava_predna_prvy),
            BitmapFactory.decodeResource(resources, R.drawable.lava_predna_druhy),
            BitmapFactory.decodeResource(resources, R.drawable.lava_predna_treti),
            BitmapFactory.decodeResource(resources, R.drawable.lava_predna_stvrty),
            BitmapFactory.decodeResource(resources, R.drawable.lava_predna_piaty)
        )
        JedLeftBottomImgs = listOf(
            BitmapFactory.decodeResource(resources, R.drawable.lava_zadna_prvy),
            BitmapFactory.decodeResource(resources, R.drawable.lava_zadna_druhy),
            BitmapFactory.decodeResource(resources, R.drawable.lava_zadna_treti),
            BitmapFactory.decodeResource(resources, R.drawable.lava_zadna_stvrty),
            BitmapFactory.decodeResource(resources, R.drawable.lava_zadna_piaty)
        )
    }

    private fun drawAndView5(imgView: ImageView, bottomImage: Bitmap, unicode: Int) {
        val tempBitmap =
            Bitmap.createBitmap(bottomImage.width, bottomImage.height, Bitmap.Config.RGB_565)
        val tempCanvas = Canvas(tempBitmap)

        tempCanvas.drawBitmap(bottomImage, 0f, 0f, null)

        val unicodeS = unicode.toString()

        for (i in unicodeS.lastIndex downTo 0) {
            val cislo = unicodeS.substring(i , i + 1).toInt()
            when (cislo) {
                0 -> continue

                in 1..5 -> {
                    when (unicodeS.length - i) {
                        1 -> tempCanvas.drawBitmap(JedLeftBottomImgs[cislo - 1], 0f, 0f, null)
                        2 -> tempCanvas.drawBitmap(DesRightBottomImgs[cislo - 1], 0f, 0f, null)
                        3 -> tempCanvas.drawBitmap(StoLeftTopImgs[cislo - 1], 0f, 0f, null)
                        4 -> tempCanvas.drawBitmap(TisRightTopImgs[cislo - 1], 0f, 0f, null)
                    }
                }
                6 -> {
                    when (unicodeS.length - i) {
                        1 -> {
                            tempCanvas.drawBitmap(JedLeftBottomImgs[0], 0f, 0f, null)
                            tempCanvas.drawBitmap(JedLeftBottomImgs[4], 0f, 0f, null)
                        }
                        2 -> {
                            tempCanvas.drawBitmap(DesRightBottomImgs[0], 0f, 0f, null)
                            tempCanvas.drawBitmap(DesRightBottomImgs[4], 0f, 0f, null)
                        }
                        3 -> {
                            tempCanvas.drawBitmap(StoLeftTopImgs[0], 0f, 0f, null)
                            tempCanvas.drawBitmap(StoLeftTopImgs[4], 0f, 0f, null)
                        }
                        4 -> {
                            tempCanvas.drawBitmap(TisRightTopImgs[0], 0f, 0f, null)
                            tempCanvas.drawBitmap(TisRightTopImgs[4], 0f, 0f, null)
                        }
                    }
                }
                7 -> {
                    when (unicodeS.length - i) {
                        1 -> {
                            tempCanvas.drawBitmap(JedLeftBottomImgs[1], 0f, 0f, null)
                            tempCanvas.drawBitmap(JedLeftBottomImgs[4], 0f, 0f, null)
                        }
                        2 -> {
                            tempCanvas.drawBitmap(DesRightBottomImgs[1], 0f, 0f, null)
                            tempCanvas.drawBitmap(DesRightBottomImgs[4], 0f, 0f, null)
                        }
                        3 -> {
                            tempCanvas.drawBitmap(StoLeftTopImgs[1], 0f, 0f, null)
                            tempCanvas.drawBitmap(StoLeftTopImgs[4], 0f, 0f, null)
                        }
                        4 -> {
                            tempCanvas.drawBitmap(TisRightTopImgs[1], 0f, 0f, null)
                            tempCanvas.drawBitmap(TisRightTopImgs[4], 0f, 0f, null)
                        }
                    }
                }
                8 -> {
                    when (unicodeS.length - i) {
                        1 -> {
                            tempCanvas.drawBitmap(JedLeftBottomImgs[2], 0f, 0f, null)
                            tempCanvas.drawBitmap(JedLeftBottomImgs[4], 0f, 0f, null)
                        }
                        2 -> {
                            tempCanvas.drawBitmap(DesRightBottomImgs[2], 0f, 0f, null)
                            tempCanvas.drawBitmap(DesRightBottomImgs[4], 0f, 0f, null)
                        }
                        3 -> {
                            tempCanvas.drawBitmap(StoLeftTopImgs[2], 0f, 0f, null)
                            tempCanvas.drawBitmap(StoLeftTopImgs[4], 0f, 0f, null)
                        }
                        4 -> {
                            tempCanvas.drawBitmap(TisRightTopImgs[2], 0f, 0f, null)
                            tempCanvas.drawBitmap(TisRightTopImgs[4], 0f, 0f, null)
                        }
                    }
                }
                9 -> {
                    when (unicodeS.length - i) {
                        1 -> {
                            tempCanvas.drawBitmap(JedLeftBottomImgs[3], 0f, 0f, null)
                            tempCanvas.drawBitmap(JedLeftBottomImgs[4], 0f, 0f, null)
                        }
                        2 -> {
                            tempCanvas.drawBitmap(DesRightBottomImgs[3], 0f, 0f, null)
                            tempCanvas.drawBitmap(DesRightBottomImgs[4], 0f, 0f, null)
                        }
                        3 -> {
                            tempCanvas.drawBitmap(StoLeftTopImgs[3], 0f, 0f, null)
                            tempCanvas.drawBitmap(StoLeftTopImgs[4], 0f, 0f, null)
                        }
                        4 -> {
                            tempCanvas.drawBitmap(TisRightTopImgs[3], 0f, 0f, null)
                            tempCanvas.drawBitmap(TisRightTopImgs[4], 0f, 0f, null)
                        }
                    }
                }
            }
        }

        imgView.setImageBitmap(tempBitmap)
    }

    private fun drawAndView4(imgView: ImageView, bottomImage: Bitmap, unicode: Int) {
        val tempBitmap =
            Bitmap.createBitmap(bottomImage.width, bottomImage.height, Bitmap.Config.RGB_565)
        val tempCanvas = Canvas(tempBitmap)

        tempCanvas.drawBitmap(bottomImage, 0f, 0f, null)

        val unicodeS = unicode.toString()

        for (i in unicodeS.lastIndex downTo 0) {
            val cislo = unicodeS.substring(i , i + 1).toInt()

            when (cislo) {
                0 -> continue

                in 1..4 -> {
                    when (unicodeS.length - i) {
                        1 -> tempCanvas.drawBitmap(JedLeftBottomImgs[cislo - 1], 0f, 0f, null)
                        2 -> tempCanvas.drawBitmap(DesRightBottomImgs[cislo - 1], 0f, 0f, null)
                        3 -> tempCanvas.drawBitmap(StoLeftTopImgs[cislo - 1], 0f, 0f, null)
                        4 -> tempCanvas.drawBitmap(TisRightTopImgs[cislo - 1], 0f, 0f, null)
                    }
                }
                5 -> {
                    when (unicodeS.length - i) {
                        1 -> {
                            tempCanvas.drawBitmap(JedLeftBottomImgs[cislo - 1], 0f, 0f, null)
                        }
                        2 -> {
                            tempCanvas.drawBitmap(DesRightBottomImgs[cislo - 1], 0f, 0f, null)
                        }
                        3 -> {
                            tempCanvas.drawBitmap(StoLeftTopImgs[0], 0f, 0f, null)
                            tempCanvas.drawBitmap(StoLeftTopImgs[3], 0f, 0f, null)
                        }
                        4 -> {
                            tempCanvas.drawBitmap(TisRightTopImgs[0], 0f, 0f, null)
                            tempCanvas.drawBitmap(TisRightTopImgs[3], 0f, 0f, null)
                        }
                    }
                }
                6 -> {
                    when (unicodeS.length - i) {
                        1 -> {
                            tempCanvas.drawBitmap(JedLeftBottomImgs[0], 0f, 0f, null)
                            tempCanvas.drawBitmap(JedLeftBottomImgs[4], 0f, 0f, null)
                        }
                        2 -> {
                            tempCanvas.drawBitmap(DesRightBottomImgs[0], 0f, 0f, null)
                            tempCanvas.drawBitmap(DesRightBottomImgs[4], 0f, 0f, null)
                        }
                        3 -> {
                            tempCanvas.drawBitmap(StoLeftTopImgs[1], 0f, 0f, null)
                            tempCanvas.drawBitmap(StoLeftTopImgs[3], 0f, 0f, null)
                        }
                        4 -> {
                            tempCanvas.drawBitmap(TisRightTopImgs[1], 0f, 0f, null)
                            tempCanvas.drawBitmap(TisRightTopImgs[3], 0f, 0f, null)
                        }
                    }
                }
                7 -> {
                    when (unicodeS.length - i) {
                        1 -> {
                            tempCanvas.drawBitmap(JedLeftBottomImgs[1], 0f, 0f, null)
                            tempCanvas.drawBitmap(JedLeftBottomImgs[4], 0f, 0f, null)
                        }
                        2 -> {
                            tempCanvas.drawBitmap(DesRightBottomImgs[1], 0f, 0f, null)
                            tempCanvas.drawBitmap(DesRightBottomImgs[4], 0f, 0f, null)
                        }
                        3 -> {
                            tempCanvas.drawBitmap(StoLeftTopImgs[2], 0f, 0f, null)
                            tempCanvas.drawBitmap(StoLeftTopImgs[3], 0f, 0f, null)
                        }
                        4 -> {
                            tempCanvas.drawBitmap(TisRightTopImgs[2], 0f, 0f, null)
                            tempCanvas.drawBitmap(TisRightTopImgs[3], 0f, 0f, null)
                        }
                    }
                }
                8 -> {
                    when (unicodeS.length - i) {
                        1 -> {
                            tempCanvas.drawBitmap(JedLeftBottomImgs[2], 0f, 0f, null)
                            tempCanvas.drawBitmap(JedLeftBottomImgs[4], 0f, 0f, null)
                        }
                        2 -> {
                            tempCanvas.drawBitmap(DesRightBottomImgs[2], 0f, 0f, null)
                            tempCanvas.drawBitmap(DesRightBottomImgs[4], 0f, 0f, null)
                        }
                        3 -> {
                            tempCanvas.drawBitmap(StoLeftTopImgs[0], 0f, 0f, null)
                            tempCanvas.drawBitmap(StoLeftTopImgs[2], 0f, 0f, null)
                            tempCanvas.drawBitmap(StoLeftTopImgs[3], 0f, 0f, null)
                        }
                        4 -> {
                            tempCanvas.drawBitmap(TisRightTopImgs[0], 0f, 0f, null)
                            tempCanvas.drawBitmap(TisRightTopImgs[2], 0f, 0f, null)
                            tempCanvas.drawBitmap(TisRightTopImgs[3], 0f, 0f, null)
                        }
                    }
                }
                9 -> {
                    when (unicodeS.length - i) {
                        1 -> {
                            tempCanvas.drawBitmap(JedLeftBottomImgs[3], 0f, 0f, null)
                            tempCanvas.drawBitmap(JedLeftBottomImgs[4], 0f, 0f, null)
                        }
                        2 -> {
                            tempCanvas.drawBitmap(DesRightBottomImgs[3], 0f, 0f, null)
                            tempCanvas.drawBitmap(DesRightBottomImgs[4], 0f, 0f, null)
                        }
                        3 -> {
                            tempCanvas.drawBitmap(StoLeftTopImgs[1], 0f, 0f, null)
                            tempCanvas.drawBitmap(StoLeftTopImgs[2], 0f, 0f, null)
                            tempCanvas.drawBitmap(StoLeftTopImgs[3], 0f, 0f, null)
                        }
                        4 -> {
                            tempCanvas.drawBitmap(TisRightTopImgs[1], 0f, 0f, null)
                            tempCanvas.drawBitmap(TisRightTopImgs[2], 0f, 0f, null)
                            tempCanvas.drawBitmap(TisRightTopImgs[3], 0f, 0f, null)
                        }
                    }
                }
            }
        }

        imgView.setImageBitmap(tempBitmap)
    }

}