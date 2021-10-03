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

    private lateinit var rightTopImgs: Array<Bitmap>
    private lateinit var rightBottomImgs: Array<Bitmap>
    private lateinit var leftTopImgs: Array<Bitmap>
    private lateinit var leftBottomImgs: Array<Bitmap>

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
        rightTopImgs = arrayOf(
            BitmapFactory.decodeResource(resources, R.drawable.prava_predna_prvy),
            BitmapFactory.decodeResource(resources, R.drawable.prava_predna_druhy),
            BitmapFactory.decodeResource(resources, R.drawable.prava_predna_treti),
            BitmapFactory.decodeResource(resources, R.drawable.prava_predna_stvrty),
            BitmapFactory.decodeResource(resources, R.drawable.prava_predna_piaty)
        )
        rightBottomImgs = arrayOf(
            BitmapFactory.decodeResource(resources, R.drawable.prava_zadna_prvy),
            BitmapFactory.decodeResource(resources, R.drawable.prava_zadna_druhy),
            BitmapFactory.decodeResource(resources, R.drawable.prava_zadna_treti),
            BitmapFactory.decodeResource(resources, R.drawable.prava_zadna_stvrty),
            BitmapFactory.decodeResource(resources, R.drawable.prava_zadna_piaty)
        )
        leftTopImgs = arrayOf(
            BitmapFactory.decodeResource(resources, R.drawable.lava_predna_prvy),
            BitmapFactory.decodeResource(resources, R.drawable.lava_predna_druhy),
            BitmapFactory.decodeResource(resources, R.drawable.lava_predna_treti),
            BitmapFactory.decodeResource(resources, R.drawable.lava_predna_stvrty),
            BitmapFactory.decodeResource(resources, R.drawable.lava_predna_piaty)
        )
        leftBottomImgs = arrayOf(
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
        for (i in unicodeS.indices) {
            val c = unicodeS.substring(i, i + 1)
            drawFinger5(c.toInt(), i, tempCanvas)
        }
        imgView.setImageBitmap(tempBitmap)
    }

    private fun drawFinger5(c: Int, i: Int, tempCanvas: Canvas) {
        when (i) {
            0 -> if (c > 5) {
                val pole = drawFinger5P(c, i)
                tempCanvas.drawBitmap(rightTopImgs[pole[0] - 1], 0f, 0f, null)
                tempCanvas.drawBitmap(rightTopImgs[pole[1] - 1], 0f, 0f, null)
            } else {
                tempCanvas.drawBitmap(rightTopImgs[c - 1], 0f, 0f, null)
            }
            1 -> if (c > 5) {
                val pole = drawFinger5P(c, i)
                tempCanvas.drawBitmap(leftTopImgs[pole[0] - 1], 0f, 0f, null)
                tempCanvas.drawBitmap(leftTopImgs[pole[1] - 1], 0f, 0f, null)
            } else {
                tempCanvas.drawBitmap(leftTopImgs[c - 1], 0f, 0f, null)
            }
            2 -> if (c > 5) {
                val pole = drawFinger5P(c, i)
                tempCanvas.drawBitmap(rightBottomImgs[pole[0] - 1], 0f, 0f, null)
                tempCanvas.drawBitmap(rightBottomImgs[pole[1] - 1], 0f, 0f, null)
            } else {
                tempCanvas.drawBitmap(rightBottomImgs[c - 1], 0f, 0f, null)
            }
            3 -> if (c > 5) {
                val pole = drawFinger5P(c, i)
                tempCanvas.drawBitmap(leftBottomImgs[pole[0] - 1], 0f, 0f, null)
                tempCanvas.drawBitmap(leftBottomImgs[pole[1] - 1], 0f, 0f, null)
            } else {
                tempCanvas.drawBitmap(leftBottomImgs[c - 1], 0f, 0f, null)
            }
        }
    }

    private fun drawFinger5P(c: Int, i: Int): IntArray {
        val a = IntArray(2)
        when (c) {
            6 -> {
                a[0] = 1
                a[1] = 5
            }
            7 -> {
                a[0] = 2
                a[1] = 5
            }
            8 -> {
                a[0] = 3
                a[1] = 5
            }
            9 -> {
                a[0] = 4
                a[1] = 5
            }
        }
        return a
    }

    private fun drawAndView4(imgView: ImageView, bottomImage: Bitmap, unicode: Int) {
        val tempBitmap =
            Bitmap.createBitmap(bottomImage.width, bottomImage.height, Bitmap.Config.RGB_565)
        val tempCanvas = Canvas(tempBitmap)
        tempCanvas.drawBitmap(bottomImage, 0f, 0f, null)
        val unicodeS = unicode.toString()
        for (i in unicodeS.indices) {
            val c = unicodeS.substring(i, i + 1)
            drawFinger4(c.toInt(), i, tempCanvas)
        }
        imgView.setImageBitmap(tempBitmap)
    }

    private fun drawFinger4(c: Int, i: Int, tempCanvas: Canvas) {
        //vykreslenie prsta
        // // treba vytvorit kombinaciu cisiel ak je c vacsie ako 4 alebo 5
        // i nam urcuje cifru s ktorou robime a c je konkretna hodnota cifry
        when (i) {
            0 -> if (c > 4) {
                val pole = drawFinger4P(c, i)
                tempCanvas.drawBitmap(rightTopImgs[pole[0] - 1], 0f, 0f, null)
                tempCanvas.drawBitmap(rightTopImgs[pole[1] - 1], 0f, 0f, null)
                // treba dat IF ak je posledna hodnota 0 aby sa nevykreslilo
                if (pole[2] != 0) tempCanvas.drawBitmap(rightTopImgs[pole[2] - 1], 0f, 0f, null)
            } else {
                tempCanvas.drawBitmap(rightTopImgs[c - 1], 0f, 0f, null)
            }
            1 -> if (c > 4) {
                val pole = drawFinger4P(c, i)
                tempCanvas.drawBitmap(leftTopImgs[pole[0] - 1], 0f, 0f, null)
                tempCanvas.drawBitmap(leftTopImgs[pole[1] - 1], 0f, 0f, null)
                if (pole[2] != 0) tempCanvas.drawBitmap(leftTopImgs[pole[2] - 1], 0f, 0f, null)
            } else {
                tempCanvas.drawBitmap(leftTopImgs[c - 1], 0f, 0f, null)
            }
            2 -> if (c > 5) {
                val pole = drawFinger4P(c, i)
                tempCanvas.drawBitmap(rightBottomImgs[pole[0] - 1], 0f, 0f, null)
                tempCanvas.drawBitmap(rightBottomImgs[pole[1] - 1], 0f, 0f, null)
                if (pole[2] != 0) tempCanvas.drawBitmap(rightBottomImgs[pole[2] - 1], 0f, 0f, null)
            } else {
                tempCanvas.drawBitmap(rightBottomImgs[c - 1], 0f, 0f, null)
            }
            3 -> if (c > 5) {
                val pole = drawFinger4P(c, i)
                tempCanvas.drawBitmap(leftBottomImgs[pole[0] - 1], 0f, 0f, null)
                tempCanvas.drawBitmap(leftBottomImgs[pole[1] - 1], 0f, 0f, null)
                if (pole[2] != 0) tempCanvas.drawBitmap(leftBottomImgs[pole[2] - 1], 0f, 0f, null)
            } else {
                tempCanvas.drawBitmap(leftBottomImgs[c - 1], 0f, 0f, null)
            }
        }
    }

    private fun drawFinger4P(c: Int, i: Int): IntArray {
        val a = IntArray(3)
        if (i == 0 || i == 1) { // horne koncatiny maju v tomto pripade 4 pazure
            when (c) {
                5 -> {
                    a[0] = 1
                    a[1] = 4
                    a[2] = 0
                }
                6 -> {
                    a[0] = 2
                    a[1] = 4
                    a[2] = 0
                }
                7 -> {
                    a[0] = 3
                    a[1] = 4
                    a[2] = 0
                }
                8 -> {
                    a[0] = 1
                    a[1] = 4
                    a[2] = 3
                }
                9 -> {
                    a[0] = 2
                    a[1] = 4
                    a[2] = 3
                }
            }
        } else {
            when (c) {
                6 -> {
                    a[0] = 1
                    a[1] = 5
                    a[2] = 0
                }
                7 -> {
                    a[0] = 2
                    a[1] = 5
                    a[2] = 0
                }
                8 -> {
                    a[0] = 3
                    a[1] = 5
                    a[2] = 0
                }
                9 -> {
                    a[0] = 4
                    a[1] = 5
                    a[2] = 0
                }
            }
        }
        return a
    }

}