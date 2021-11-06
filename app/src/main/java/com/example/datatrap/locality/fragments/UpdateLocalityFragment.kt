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
import com.example.datatrap.models.Project
import com.example.datatrap.viewmodels.LocalityViewModel
import com.example.datatrap.viewmodels.ProjectLocalityViewModel
import com.example.datatrap.viewmodels.ProjectViewModel
import java.util.*

class UpdateLocalityFragment : Fragment(){

    private var _binding: FragmentUpdateLocalityBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var localityViewModel: LocalityViewModel
    private lateinit var prjLocalityViewModel: ProjectLocalityViewModel
    private lateinit var projectViewModel: ProjectViewModel
    private val args by navArgs<UpdateLocalityFragmentArgs>()
    private lateinit var gpsProvider: GPSProvider

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        _binding = FragmentUpdateLocalityBinding.inflate(inflater, container, false)
        localityViewModel = ViewModelProvider(this).get(LocalityViewModel::class.java)
        prjLocalityViewModel = ViewModelProvider(this).get(ProjectLocalityViewModel::class.java)
        projectViewModel = ViewModelProvider(this).get(ProjectViewModel::class.java)

        gpsProvider = GPSProvider(requireContext(), requireActivity(), this)

        initLocalityValuesToView()

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

    private fun initLocalityValuesToView(){
        binding.etLocalityName.setText(args.locality.localityName)
        binding.etLocalityNote.setText(args.locality.note)
        binding.etSessionNum.setText(args.locality.numSessions.toString())
        binding.tvLatitude.text = args.locality.x.toString()
        binding.tvLongnitude.text = args.locality.y.toString()
    }

    private fun deleteLocality() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes"){_, _ ->

            // zmensit numLocal vo vsetkych projektoch ktore su vo vztahu s vymazavanou lokalitou
            updateProjectsNumLocal()

            // odstranit lokalitu
            localityViewModel.deleteLocality(args.locality)

            Toast.makeText(requireContext(),"Locality deleted.", Toast.LENGTH_LONG).show()
            findNavController().navigateUp()
        }
            .setNegativeButton("No"){_, _ -> }
            .setTitle("Delete Locality?")
            .setMessage("Are you sure you want to delete this locality?")
            .create().show()
    }

    private fun updateProjectsNumLocal(){
        val updateProjectList: List<Project> = prjLocalityViewModel.getProjectsForLocality(args.locality.localityId).value?.first()?.projects!!

        updateProjectList.forEach {
            it.numLocal = (it.numLocal - 1)
            it.projectDateTimeUpdated = Calendar.getInstance().time
            projectViewModel.updateProject(it)
        }
    }

    private fun updateLocality() {
        val localityName = binding.etLocalityName.text.toString()
        val latitude = binding.tvLatitude.text.toString()
        val longitude = binding.tvLongnitude.text.toString()

        if (checkInput(localityName, latitude, longitude)){
            val locality: Locality = args.locality.copy()
            locality.localityName = localityName
            locality.x = latitude.toFloat()
            locality.y = longitude.toFloat()
            locality.localityDateTimeUpdated = Calendar.getInstance().time
            locality.note = if (binding.etLocalityNote.text.toString().isBlank()) null else binding.etLocalityNote.text.toString()

            localityViewModel.updateLocality(locality)

            Toast.makeText(requireContext(), "Locality updated.", Toast.LENGTH_SHORT).show()

            findNavController().navigateUp()
        }else{
            Toast.makeText(requireContext(), getString(R.string.emptyFields), Toast.LENGTH_LONG).show()
        }
    }

    private fun checkInput(
        localityName: String,
        latitude: String,
        longnitude: String
    ): Boolean {
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