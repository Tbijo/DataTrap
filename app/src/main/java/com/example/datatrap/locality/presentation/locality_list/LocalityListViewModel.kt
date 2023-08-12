package com.example.datatrap.locality.presentation.locality_list

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datatrap.R
import com.example.datatrap.core.data.pref.PrefViewModel
import com.example.datatrap.locality.data.Locality
import com.example.datatrap.locality.data.LocList
import com.example.datatrap.locality.data.LocalityRepository
import com.example.datatrap.project.data.ProjectLocalityCrossRef
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import javax.inject.Inject

@HiltViewModel
class LocalityListViewModel @Inject constructor (
    private val localityRepository: LocalityRepository
): ViewModel() {

    val localityList: LiveData<List<LocList>> = localityRepository.localityList
    private lateinit var requestMultiplePermissions: ActivityResultLauncher<Array<String>>
    private lateinit var localityList: List<LocList>
    private val localityListViewModel: LocalityListViewModel by viewModels()
    private val prefViewModel: PrefViewModel by viewModels()

    init {
        holder.binding.tvName.text = currenItem.localityName
        holder.binding.tvDate.text = SimpleDateFormat.getDateTimeInstance().format(currenItem.dateTime)
        holder.binding.tvNumSes.text = currenItem.numSessions.toString()

        localityListViewModel.localityList.observe(viewLifecycleOwner) { localities ->
            adapter.setData(localities)
            localityList = localities
        }

        adapter.setOnItemClickListener(object : PrjLocalityRecyclerAdapter.MyClickListener {
            override fun useClickListener(position: Int) {
                // tu sa vytvori kombinacia project a locality a pojde sa spat do PrjLocality
                insertCombination(position)

                Toast.makeText(requireContext(), "Association created.", Toast.LENGTH_SHORT).show()

                findNavController().navigateUp()
            }

            override fun useLongClickListener(position: Int) {
                // tu sa pojde upravit alebo vymazat lokalita
                val locality: LocList = localityList[position]
                val action = ListAllLocalityFragmentDirections.actionListAllLocalityFragmentToUpdateLocalityFragment(locality)
                findNavController().navigate(action)
            }

        })

        binding.addLocalityFloatButton.setOnClickListener {
            // tu sa pojde vytvorit nova lokalita
            val action = ListAllLocalityFragmentDirections.actionListAllLocalityFragmentToAddLocalityFragment()
            findNavController().navigate(action)
        }

        requestMultiplePermissions = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { perms ->
            perms.entries.forEach {
                Log.d("PERMISSIONS RESULT", "${it.key} = ${it.value}")
            }
        }

        R.id.menu_map -> goToMap()

        // Other one
        binding.tvPathPrjName.text = args.project.projectName

        prjLocalityViewModel.getLocalitiesForProject(args.project.projectId)
            .observe(viewLifecycleOwner) {
                val locList = mutableListOf<LocList>()
                it.first().localities.forEach { locality ->
                    val loc = LocList(
                        locality.localityId,
                        locality.localityName,
                        locality.localityDateTimeCreated,
                        locality.xA,
                        locality.yA,
                        locality.numSessions
                    )
                    locList.add(loc)
                }
                adapter.setData(locList)
                localityList = locList
            }

        adapter.setOnItemClickListener(object : PrjLocalityRecyclerAdapter.MyClickListener {

            override fun useClickListener(position: Int) {
                // nastavit vybranu lokalitu
                prefViewModel.saveLocNamePref(localityList[position].localityName)
                // presun do sessionov s projektom a lokalitou
                goToSession(position)
            }

            override fun useLongClickListener(position: Int) {
                val builder = AlertDialog.Builder(requireContext())
                builder.setPositiveButton("Yes") { _, _ ->

                    // vymazat kombinaciu projektu a vybranej lokality
                    deleteCombination(position)

                    Toast.makeText(requireContext(), "Association deleted.", Toast.LENGTH_LONG)
                        .show()
                }
                    .setNegativeButton("No") { _, _ -> }
                    .setTitle("Delete Association?")
                    .setMessage("Are you sure you want to delete this association?")
                    .create().show()
            }
        })

        binding.addLocalityFloatButton.setOnClickListener {
            // prechod do vsetkych lokalit na pracu s lokalitami a vytvorenie kombinacie project a locality
            val action =
                ListPrjLocalityFragmentDirections.actionListPrjLocalityFragmentToListAllLocalityFragment(
                    args.project
                )
            findNavController().navigate(action)
        }
    }

    private fun goToSession(position: Int) {
        val action =
            ListPrjLocalityFragmentDirections.actionListPrjLocalityFragmentToListPrjSessionFragment(
                args.project,
                localityList[position]
            )
        findNavController().navigate(action)
    }

    private fun deleteCombination(position: Int) {
        val projectLocalityCrossRef =
            ProjectLocalityCrossRef(args.project.projectId, localityList[position].localityId)
        prjLocalityViewModel.deleteProjectLocality(projectLocalityCrossRef)
    }

    fun onQueryTextChange(newText: String?): Boolean {
        if (newText != null) {
            searchLocalities(newText)
        }
        return true
    }

    private fun insertCombination(position: Int) {
        val projectLocalityCrossRef = ProjectLocalityCrossRef(args.project.projectId, localityList[position].localityId)
        // vytvorit kombinaciu
        prjLocalityViewModel.insertProjectLocality(projectLocalityCrossRef)
    }

    private fun goToMap() {
        if (hasPermissions(Manifest.permission.ACCESS_FINE_LOCATION)) {
            val action = ListAllLocalityFragmentDirections.actionListAllLocalityFragmentToLocalityMapFragment(
                localityList.toTypedArray()
            )
            findNavController().navigate(action)
        }
        else {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION))
        }
    }

    private fun searchLocalities(query: String?) {
        val searchQuery = "%$query%"
        localityListViewModel.searchLocalities(searchQuery).observe(viewLifecycleOwner) { localities ->
            localities.let {
                adapter.setData(it)
            }
        }
    }

    ///////////////////////////////PERMISSIONS////////////////////////////////////////

    private fun hasPermissions(vararg permissions: String): Boolean {
        permissions.forEach {
            if (ContextCompat.checkSelfPermission(requireContext(), it) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    private fun requestPermissions(permissions: Array<String>) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                permissions[0]
            )
        ) {
            val builder = AlertDialog.Builder(requireContext())
            builder.setMessage("Application needs this permission to work.")
                .setPositiveButton("OK") { _, _ ->
                    requestAppSettings()
                }
                .setNegativeButton("Cancel") { _, _ ->
                    Toast.makeText(requireContext(), "Application can not proceed.", Toast.LENGTH_LONG)
                        .show()
                }
                .create().show()
            Log.d("PERMISSIONS", "DOUBLE DENIAL")

        } else {
            Log.d("PERMISSIONS", "GETTING PERMISSIONS")
            requestMultiplePermissions.launch(
                permissions
            )
        }
    }

    private fun requestAppSettings(){
        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", requireContext().packageName, null)
        )
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    fun insertLocality(locality: Locality) {
        viewModelScope.launch(Dispatchers.IO){
            localityRepository.insertLocality(locality)
        }
    }

    fun updateLocality(locality: Locality) {
        viewModelScope.launch(Dispatchers.IO){
            localityRepository.updateLocality(locality)
        }
    }

    fun deleteLocality(localityId: Long) {
        viewModelScope.launch(Dispatchers.IO){
            localityRepository.deleteLocality(localityId)
        }
    }

    fun getLocality(localityId: Long): LiveData<Locality> {
        return localityRepository.getLocality(localityId)
    }

    fun searchLocalities(localityName: String): LiveData<List<LocList>> {
        return localityRepository.searchLocalities(localityName)
    }
}