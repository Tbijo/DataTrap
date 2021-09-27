package com.example.datatrap.locality.fragments

import android.os.Bundle
import android.provider.Settings
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.datatrap.R
import com.example.datatrap.databinding.FragmentAddLocalityBinding
import com.example.datatrap.locality.fragments.gps.GPSProvider
import com.example.datatrap.models.Locality
import com.example.datatrap.viewmodels.LocalityViewModel
import java.util.*

class AddLocalityFragment : Fragment() {

    private var _binding: FragmentAddLocalityBinding? = null
    private val binding get() = _binding!!
    private lateinit var localityViewModel: LocalityViewModel
    private lateinit var gpsProvider: GPSProvider

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        _binding = FragmentAddLocalityBinding.inflate(inflater, container, false)
        localityViewModel = ViewModelProvider(this).get(LocalityViewModel::class.java)

        gpsProvider = GPSProvider(requireContext(), requireActivity(), this)

        binding.btnGetCoordinates.setOnClickListener {
            // ziskanie aktualnych suradnic
            getCoordinates()
        }

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_save -> insertLocality()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun insertLocality() {
        val localityName = binding.etLocalityName.text.toString()
        val localityNote = binding.etLocalityNote.text.toString()
        val latitude = binding.tvLatitude.text.toString()
        val longnitude = binding.tvLongnitude.text.toString()
        val deviceID: String = Settings.Secure.getString(requireContext().contentResolver, Settings.Secure.ANDROID_ID)

        if (checkInput(localityName, latitude, longnitude)){

            val locality = Locality(0, localityName, deviceID, Calendar.getInstance().time,
                Integer.parseInt(latitude).toFloat(),
                Integer.parseInt(longnitude).toFloat(),0, localityNote)

            localityViewModel.insertLocality(locality)

            Toast.makeText(requireContext(), "New locality added.", Toast.LENGTH_SHORT).show()

            findNavController().navigateUp()
        }else{
            Toast.makeText(requireContext(), getString(R.string.emptyFields), Toast.LENGTH_LONG).show()
        }
    }

    private fun checkInput(localityName: String, latitude: String, longnitude: String): Boolean {
        return localityName.isNotEmpty() && latitude.isNotEmpty() && longnitude.isNotEmpty()
    }

    private fun getCoordinates(){
        gpsProvider.getCoordinates(object : GPSProvider.CoordinatesListener{
            override fun onReceivedCoordinates(latitude: Double, longitude: Double) {
                binding.tvLatitude.text = latitude.toString()
                binding.tvLongnitude.text = longitude.toString()
            }

        })
    }

}