package com.example.datatrap.locality.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.datatrap.R
import com.example.datatrap.databinding.FragmentUpdateLocalityBinding
import com.example.datatrap.locality.fragments.gps.GPSProvider
import com.example.datatrap.models.Locality
import com.example.datatrap.models.tuples.LocList
import com.example.datatrap.viewmodels.LocalityViewModel
import java.util.*

class UpdateLocalityFragment : Fragment(){

    private var _binding: FragmentUpdateLocalityBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var localityViewModel: LocalityViewModel
    private val args by navArgs<UpdateLocalityFragmentArgs>()
    private lateinit var gpsProviderA: GPSProvider
    //private lateinit var gpsProviderB: GPSProvider
    private lateinit var currentLocality: Locality

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        _binding = FragmentUpdateLocalityBinding.inflate(inflater, container, false)
        localityViewModel = ViewModelProvider(this).get(LocalityViewModel::class.java)

        gpsProviderA = GPSProvider(requireContext(), requireActivity(), this)
        //gpsProviderB = GPSProvider(requireContext(), requireActivity(), this)

        localityViewModel.getLocality(args.locList.localityId).observe(viewLifecycleOwner, {
            initLocalityValuesToView(it)
            currentLocality = it
        })

        binding.btnGetCoorA.setOnClickListener {
            getCoordinatesA()
        }

        binding.btnUpGetCoorB.setOnClickListener {
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
        inflater.inflate(R.menu.update_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_save -> updateLocality()
            R.id.menu_delete -> deleteLocality()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initLocalityValuesToView(locality: Locality){
        binding.etLocalityName.setText(locality.localityName)
        binding.etLocalityNote.setText(locality.note.toString())
        binding.etSessionNum.setText(locality.numSessions.toString())

        binding.etUpLatA.setText(locality.xA.toString())
        binding.etUpLongA.setText(locality.yA.toString())
        binding.etUpLatB.setText(locality.xB.toString())
        binding.etUpLongB.setText(locality.yB.toString())
    }

    private fun deleteLocality() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes"){_, _ ->

            // odstranit lokalitu
            localityViewModel.deleteLocality(args.locList.localityId)

            Toast.makeText(requireContext(),"Locality deleted.", Toast.LENGTH_LONG).show()
            findNavController().navigateUp()
        }
            .setNegativeButton("No"){_, _ -> }
            .setTitle("Delete Locality?")
            .setMessage("Are you sure you want to delete this locality?")
            .create().show()
    }

    private fun updateLocality() {
        val localityName = binding.etLocalityName.text.toString()
        val latitudeA = binding.etUpLatA.text.toString()
        val longitudeA = binding.etUpLongA.text.toString()
        val latitudeB = binding.etUpLatB.text.toString()
        val longitudeB = binding.etUpLongB.text.toString()

        if (checkInput(localityName, latitudeA, longitudeA, latitudeB, longitudeB)){
            val locality: Locality = currentLocality.copy()
            locality.localityName = localityName
            locality.numSessions = Integer.parseInt(binding.etSessionNum.text.toString())
            locality.xA = latitudeA.toFloat()
            locality.yA = longitudeA.toFloat()
            locality.xB = latitudeB.toFloat()
            locality.yB = longitudeB.toFloat()
            locality.localityDateTimeUpdated = Calendar.getInstance().time
            locality.note = if (binding.etLocalityNote.text.toString().isBlank() || binding.etLocalityNote.text.toString() == "null") null else binding.etLocalityNote.text.toString()

            localityViewModel.updateLocality(locality)

            Toast.makeText(requireContext(), "Locality updated.", Toast.LENGTH_SHORT).show()

            gpsProviderA.cancelLocationRequest()
            //gpsProviderB.cancelLocationRequest()

            findNavController().navigateUp()
        }else{
            Toast.makeText(requireContext(), getString(R.string.emptyFields), Toast.LENGTH_LONG).show()
        }
    }

    private fun checkInput(
        localityName: String,
        latitudeA: String,
        longnitudeA: String,
        latitudeB: String,
        longnitudeB: String
    ): Boolean {
        return localityName.isNotEmpty() && latitudeA.isNotEmpty() && longnitudeA.isNotEmpty() &&
                latitudeB.isNotEmpty() && longnitudeB.isNotEmpty()
    }

    private fun getCoordinatesA() {
        gpsProviderA.getCoordinates(object : GPSProvider.CoordinatesListener{
            override fun onReceivedCoordinates(latitude: Double, longitude: Double) {
                binding.etUpLatA.setText(latitude.toString())
                binding.etUpLongA.setText(longitude.toString())
            }
        })
    }

    private fun getCoordinatesB() {
        gpsProviderA.getCoordinates(object : GPSProvider.CoordinatesListener{
            override fun onReceivedCoordinates(latitude: Double, longitude: Double) {
                binding.etUpLatB.setText(latitude.toString())
                binding.etUpLongB.setText(longitude.toString())
            }
        })
//        gpsProviderB.getCoordinates(object : GPSProvider.CoordinatesListener{
//            override fun onReceivedCoordinates(latitude: Double, longitude: Double) {
//                binding.etUpLatB.setText(latitude.toString())
//                binding.etUpLongB.setText(longitude.toString())
//            }
//        })
    }

}