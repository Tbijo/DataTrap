package com.example.datatrap.locality.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.datatrap.R
import com.example.datatrap.databinding.FragmentAddLocalityBinding
import com.example.datatrap.models.Locality
import com.example.datatrap.viewmodels.LocalityViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.text.SimpleDateFormat
import java.util.*

class AddLocalityFragment : Fragment(), EasyPermissions.PermissionCallbacks {

    /*Este by bolo treba dat nejaku kontrolu ci je GPS zapate*/

    private var _binding: FragmentAddLocalityBinding? = null
    private val binding get() = _binding!!
    private lateinit var localityViewModel: LocalityViewModel
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        _binding = FragmentAddLocalityBinding.inflate(inflater, container, false)
        localityViewModel = ViewModelProvider(this).get(LocalityViewModel::class.java)
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())

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
        val localityDate = getDate()
        val localityNote = binding.etLocalityNote.text.toString()
        val latitude = binding.tvLatitude.text.toString()
        val longnitude = binding.tvLongnitude.text.toString()

        if (checkInput(localityName, localityDate, latitude, longnitude)){
            val locality = Locality(0, localityName, localityDate,
                Integer.parseInt(latitude).toFloat(),
                Integer.parseInt(longnitude).toFloat(),0, localityNote)
            localityViewModel.insertLocality(locality)
            Toast.makeText(requireContext(), "New locality added.", Toast.LENGTH_SHORT).show()

            findNavController().navigateUp()
        }else{
            Toast.makeText(requireContext(), getString(R.string.emptyFields), Toast.LENGTH_LONG).show()
        }
    }

    private fun checkInput(localityName: String, localityDate: String, latitude: String, longnitude: String): Boolean {
        return localityName.isNotEmpty() && localityDate.isNotEmpty() && latitude.isNotEmpty() && longnitude.isNotEmpty()
    }

    private fun getDate(): String{
        val date = Calendar.getInstance().time
        val formatter = SimpleDateFormat.getDateInstance()
        return formatter.format(date)
    }

    @SuppressLint("MissingPermission")
    private fun getCoordinates() {
        if (hasLocationPermission()) {
            // ak mame povolenie mozme zobrazit suradnice
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                binding.tvLatitude.text = location.latitude.toString()
                binding.tvLongnitude.text = location.longitude.toString()
            }
        } else {
            // ak nie tak si ho vyziadame
            requestLocationPermission()
        }
    }

    // tato funkcia vrati true ak su povolenia dane a false ak nie su
    private fun hasLocationPermission() =
        EasyPermissions.hasPermissions(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION    // sem pojdu permissiony ktore chceme skontrolovat ci su povolene
        )

    // vyziadanie si povoleni
    private fun requestLocationPermission() {
        EasyPermissions.requestPermissions(
            this,
            "This app can not work without Location Permission.", // tuto bude odkaz pre pouzivatela ak neda povolenie po
            1,
            Manifest.permission.ACCESS_FINE_LOCATION    // co chceme povolit
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // musime len poslat vsetky parametre aby kniznica EasyPermissions vedela pracovat s nasimi runtime permissions
        // posledny parameter je tento fragment
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    // bude zavolana ked pouzivatel da povolenie
    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        Toast.makeText(requireContext(), "Permission Granted.", Toast.LENGTH_SHORT).show()
    }

    // bude zavolana ked pouzivatel zamietne povolenie
    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            // ak tato funkcia vrati true znamena to ze uzivatel permanentne zablokoval ziadanu permission
            // tak mu dame nastavenia aby ich potom sam manualne povolil
            AppSettingsDialog.Builder(requireActivity()).build().show()
        } else {
            // inak len vyzadujeme permission
            requestLocationPermission()
        }
    }
}