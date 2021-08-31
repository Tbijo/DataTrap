package com.example.datatrap.locality.fragments

import android.Manifest
import android.annotation.SuppressLint
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
import com.example.datatrap.models.Locality
import com.example.datatrap.viewmodels.LocalityViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

class UpdateLocalityFragment : Fragment(), EasyPermissions.PermissionCallbacks {

    /*Este by bolo treba dat nejaku kontrolu ci je GPS zapate*/

    private var _binding: FragmentUpdateLocalityBinding? = null
    private val binding get() = _binding!!
    private lateinit var localityViewModel: LocalityViewModel
    private val args by navArgs<UpdateLocalityFragmentArgs>()
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        _binding = FragmentUpdateLocalityBinding.inflate(inflater, container, false)
        localityViewModel = ViewModelProvider(this).get(LocalityViewModel::class.java)
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())

        binding.etLocalityName.setText(args.locality.localityName)
        binding.etLocalityNote.setText(args.locality.note)
        binding.etSessionNum.setText(args.locality.numSessions.toString())
        binding.tvLatitude.text = args.locality.x.toString()
        binding.tvLongnitude.text = args.locality.y.toString()

        binding.btnGetCoordinates.setOnClickListener {
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
        inflater.inflate(R.menu.update_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_save -> updateLocality()
            R.id.menu_delete -> deleteLocality()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteLocality() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes"){_, _ ->

            localityViewModel.deleteLocality(args.locality)

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
        val localityDate = args.locality.date
        val localityNote = binding.etLocalityNote.text.toString()
        val latitude = binding.tvLatitude.text.toString()
        val longnitude = binding.tvLongnitude.text.toString()

        if (checkInput(localityName, localityDate, latitude, longnitude)){
            val locality = Locality(args.locality.localityId, localityName, localityDate,
                Integer.parseInt(latitude).toFloat(),
                Integer.parseInt(longnitude).toFloat(),args.locality.numSessions, localityNote)
            localityViewModel.updateLocality(locality)
            Toast.makeText(requireContext(), "Locality updated.", Toast.LENGTH_SHORT).show()

            findNavController().navigateUp()
        }else{
            Toast.makeText(requireContext(), getString(R.string.emptyFields), Toast.LENGTH_LONG).show()
        }
    }

    private fun checkInput(
        localityName: String,
        localityDate: String,
        latitude: String,
        longnitude: String
    ): Boolean {
        return localityName.isNotEmpty() && localityDate.isNotEmpty() && latitude.isNotEmpty() && longnitude.isNotEmpty()
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