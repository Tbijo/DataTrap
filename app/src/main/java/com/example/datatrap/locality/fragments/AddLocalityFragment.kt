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
    private lateinit var gpsProviderA: GPSProvider
    //private lateinit var gpsProviderB: GPSProvider

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        _binding = FragmentAddLocalityBinding.inflate(inflater, container, false)
        localityViewModel = ViewModelProvider(this).get(LocalityViewModel::class.java)

        gpsProviderA = GPSProvider(requireContext(), requireActivity(), this)
        //gpsProviderB = GPSProvider(requireContext(), requireActivity(), this)

        binding.btnGetCoorA.setOnClickListener {
            // ziskanie aktualnych suradnic
            getCoordinatesA()
        }

        binding.btnGetCoorB.setOnClickListener {
            getCoordinatesB()
        }

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        gpsProviderA.cancelLocationRequest()
        //gpsProviderB.cancelLocationRequest()
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
        val latitudeA = binding.etLatitudeA.text.toString()
        val longnitudeA = binding.etLongnitudeA.text.toString()
        val latitudeB = binding.etLatitudeB.text.toString()
        val longnitudeB = binding.etLongnitudeB.text.toString()

        if (checkInput(localityName, latitudeA, longnitudeA, latitudeB, longnitudeB)){
            val localityNote = if (binding.etLocalityNote.text.toString().isBlank()) null else binding.etLocalityNote.text.toString()
            val deviceID: String = Settings.Secure.getString(requireContext().contentResolver, Settings.Secure.ANDROID_ID)

            val locality = Locality(0, localityName, deviceID, Calendar.getInstance().time,
                null, latitudeA.toFloat(), longnitudeA.toFloat(),
                latitudeB.toFloat(), longnitudeB.toFloat(),
                0, localityNote)

            localityViewModel.insertLocality(locality)

            Toast.makeText(requireContext(), "New locality added.", Toast.LENGTH_SHORT).show()

            gpsProviderA.cancelLocationRequest()
            //gpsProviderB.cancelLocationRequest()

            findNavController().navigateUp()
        }else{
            Toast.makeText(requireContext(), getString(R.string.emptyFields), Toast.LENGTH_LONG).show()
        }
    }

    private fun checkInput(localityName: String, latitudeA: String, longnitudeA: String, latitudeB: String, longnitudeB: String): Boolean {
        return localityName.isNotEmpty() && latitudeA.isNotEmpty() && longnitudeA.isNotEmpty() && latitudeB.isNotEmpty() && longnitudeB.isNotEmpty()
    }

    private fun getCoordinatesA() {
        gpsProviderA.getCoordinates(object : GPSProvider.CoordinatesListener{
            override fun onReceivedCoordinates(latitude: Double, longitude: Double) {
                binding.etLatitudeA.setText(latitude.toString())
                binding.etLongnitudeA.setText(longitude.toString())
            }
        })
    }

    private fun getCoordinatesB() {
        gpsProviderA.getCoordinates(object : GPSProvider.CoordinatesListener{
            override fun onReceivedCoordinates(latitude: Double, longitude: Double) {
                binding.etLatitudeB.setText(latitude.toString())
                binding.etLongnitudeB.setText(longitude.toString())
            }
        })
//        gpsProviderB.getCoordinates(object : GPSProvider.CoordinatesListener{
//            override fun onReceivedCoordinates(latitude: Double, longitude: Double) {
//                binding.etLatitudeB.setText(latitude.toString())
//                binding.etLongnitudeB.setText(longitude.toString())
//            }
//        })
    }

}