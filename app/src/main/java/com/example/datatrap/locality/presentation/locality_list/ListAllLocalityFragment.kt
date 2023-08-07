package com.example.datatrap.locality.presentation.locality_list

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.datatrap.R
import com.example.datatrap.databinding.FragmentListAllLocalityBinding
import com.example.datatrap.locality.presentation.locality_prj_list.PrjLocalityRecyclerAdapter
import com.example.datatrap.locality.presentation.locality_prj_list.ProjectLocalityViewModel
import com.example.datatrap.project.data.ProjectLocalityCrossRef
import com.example.datatrap.locality.data.LocList
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListAllLocalityFragment : Fragment(), SearchView.OnQueryTextListener {

    private var _binding: FragmentListAllLocalityBinding? = null
    private val binding get() = _binding!!

    private val localityListViewModel: LocalityListViewModel by viewModels()
    private val prjLocalityViewModel: ProjectLocalityViewModel by viewModels()

    private lateinit var adapter: PrjLocalityRecyclerAdapter
    private val args by navArgs<ListAllLocalityFragmentArgs>()
    private lateinit var requestMultiplePermissions: ActivityResultLauncher<Array<String>>

    private lateinit var localityList: List<LocList>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        _binding = FragmentListAllLocalityBinding.inflate(inflater, container, false)

        adapter = PrjLocalityRecyclerAdapter()
        val recyclerView = binding.localityRecyclerview
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val dividerItemDecoration = DividerItemDecoration(
            recyclerView.context,
            LinearLayoutManager.VERTICAL
        )
        recyclerView.addItemDecoration(dividerItemDecoration)

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

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_locality_menu, menu)
        val searchItem = menu.findItem(R.id.menu_search)
        val searchView = searchItem?.actionView as? SearchView
        searchView?.setOnQueryTextListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_map -> goToMap()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
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

}