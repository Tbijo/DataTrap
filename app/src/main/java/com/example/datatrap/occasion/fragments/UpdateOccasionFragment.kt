package com.example.datatrap.occasion.fragments

import android.app.AlertDialog
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.datatrap.R
import com.example.datatrap.databinding.FragmentUpdateOccasionBinding
import com.example.datatrap.viewmodels.OccasionViewModel
import com.example.datatrap.viewmodels.PictureViewModel

class UpdateOccasionFragment : Fragment() {

    private var _binding: FragmentUpdateOccasionBinding? = null
    private val binding get() = _binding!!
    private lateinit var occasionViewModel: OccasionViewModel
    private lateinit var pictureViewModel: PictureViewModel
    private val args by navArgs<UpdateOccasionFragmentArgs>()

    private var imageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        _binding = FragmentUpdateOccasionBinding.inflate(inflater, container, false)
        occasionViewModel = ViewModelProvider(this).get(OccasionViewModel::class.java)
        pictureViewModel = ViewModelProvider(this).get(PictureViewModel::class.java)

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.delete_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_delete -> deleteOccasion()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteOccasion() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes"){_, _ ->

            occasionViewModel.deleteOccasion(args.occasion)

            Toast.makeText(requireContext(),"Occasion deleted.", Toast.LENGTH_LONG).show()

            findNavController().navigateUp()
        }
            .setNegativeButton("No"){_, _ -> }
            .setTitle("Delete Occasion?")
            .setMessage("Are you sure you want to delete this occasion?")
            .create().show()
    }

}