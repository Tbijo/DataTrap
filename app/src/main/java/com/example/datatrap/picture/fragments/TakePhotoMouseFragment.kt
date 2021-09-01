package com.example.datatrap.picture.fragments

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.datatrap.R
import com.example.datatrap.databinding.FragmentTakePhotoMouseBinding
import com.example.datatrap.viewmodels.PictureViewModel
import com.example.datatrap.viewmodels.SharedViewModel

class TakePhotoMouseFragment : Fragment() {

    private var _binding: FragmentTakePhotoMouseBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var pictureViewModel: PictureViewModel

    private var myPath: String? = null
    private  var photoURI: Uri? = null
    private var title: String? = null
    private var imgName: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_take_photo_mouse, container, false)
    }

}